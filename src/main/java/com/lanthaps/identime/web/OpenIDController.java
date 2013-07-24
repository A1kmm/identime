package com.lanthaps.identime.web;

import java.net.*;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.openid4java.message.*;
import org.openid4java.server.ServerManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.lanthaps.identime.model.LocalUser;
import com.lanthaps.identime.model.UserSiteApproval;
import com.lanthaps.identime.repository.LocalUserRepository;
import com.lanthaps.identime.repository.UserSiteApprovalRepository;
import com.lanthaps.identime.service.CSRFTokenService;
import com.lanthaps.identime.service.SettingService;
import com.lanthaps.identime.service.SettingServiceImpl;

/**
 * The OpenIDController handles OpenID protocol requests, and also non-OpenID
 * requests to URLs that are usually for OpenID use.
 * @author Andrew Miller
 *
 */
@Controller
public class OpenIDController {
  OpenIDController() {
    openIDManager = new ServerManager();
  }
	
  private static Logger logger = LoggerFactory.getLogger(OpenIDController.class);
  
  @Autowired private SettingService settingService;
  private ServerManager openIDManager;
  @Autowired private UserSiteApprovalRepository userSiteApprovalRepository;
  @Autowired private LocalUserRepository localUserRepository;
  @Autowired private RequestCache requestCache;
  @Autowired private CSRFTokenService csrfTokenService;
  
  @RequestMapping(value = "/u/{userName}", method = {RequestMethod.GET, RequestMethod.HEAD},
      produces="application/xrds+xml")
  public String doYadis(Model model) {
    return "discover";
  }

  @RequestMapping(value = "/u/{userName}", method = RequestMethod.GET)
  public String doUserURL(Model model) {
    return "user-notyadis";
  }

  private String raw(Model m, String v) {
    m.addAttribute("rawText", v);
    return "raw";
  }

  @RequestMapping(value = "/approveSite", method = RequestMethod.POST) @Transactional
  public String providerRetry(HttpSession session, Model m, @RequestParam(required=true) String siteEndpoint, HttpServletRequest req) {
    if (!csrfTokenService.checkToken(req))
      return "login";
    LocalUser authorisingUser = localUserRepository.findOne(
        SecurityContextHolder.getContext().getAuthentication().getName());
    List<UserSiteApproval> existing =
        userSiteApprovalRepository.findByAuthorisingUserAndSiteEndpoint(authorisingUser, siteEndpoint);
    UserSiteApproval approval = (existing.isEmpty()) ? new UserSiteApproval() : existing.get(0);
    approval.setApprovalTimestamp(new Date());
    approval.setAuthorisingUser(authorisingUser);
    approval.setSiteEndpoint(siteEndpoint);
    userSiteApprovalRepository.save(approval);
    
    HttpServletRequest pending = (HttpServletRequest)session.getAttribute("openid-pending-request");
    if (pending == null)
      return "home";
    else
      return doOpenID(m, pending);
  }
  
  @RequestMapping(value = "/provider", method = {RequestMethod.GET, RequestMethod.HEAD,
      RequestMethod.POST}) @Transactional
  public String doOpenID(Model m, HttpServletRequest req) {
    ParameterList request = new ParameterList(req.getParameterMap());

    String mode = request.getParameterValue("openid.mode");
    if ("associate".equals(mode))
      return raw(m, openIDManager.associationResponse(request).keyValueFormEncoding());
    else if ("check_authentication".equals(mode))
      return raw(m, openIDManager.verify(request).keyValueFormEncoding());
    else if ("checkid_immediate".equals(mode))
      return doCheckID(m, request, req, true);
    else if ("checkid_setup".equals(mode))
      return doCheckID(m, request, req, false);
    else
      return raw(m, DirectError.createDirectError("Relying Party sent bad openid.mode").keyValueFormEncoding());
  }

  private String doCheckID(Model m, ParameterList request, HttpServletRequest rawReq, boolean isImmediate) {
    AuthRequest authRequest;
    try {
      authRequest = AuthRequest.createAuthRequest(request, openIDManager.getRealmVerifier());
    } catch (MessageException e) {
      return raw(m, DirectError.createDirectError("Invalid authorization request").keyValueFormEncoding());
    }
    
    if (authRequest.getIdentity() == null)
      return raw(m, DirectError.createDirectError("Identity to check required.").keyValueFormEncoding());
    
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    
    try {
      if (auth == null || !auth.isAuthenticated())
        return doLoginRequest(m, authRequest, rawReq);

      String baseURL = settingService.loadStringSetting(SettingServiceImpl.baseURL);
      // We do this here because the setting is mutable (and we currently have
      // no pubsub hook on settings changing).
      openIDManager.setOPEndpointUrl(baseURL + "/provider");
      
      String matchPath = new URI(baseURL).getPath();
      if (!matchPath.endsWith("/"))
        matchPath += "/";
      matchPath += "u/";
      // Now we get the user's authenticated username, and escape it...
      String username = SecurityContextHolder.getContext().getAuthentication().getName();
      matchPath += URLEncoder.encode(username, "UTF-8");
      logger.info("Current identity is " + matchPath + " requested is " +
          new URI(authRequest.getIdentity()).getPath());
      if (!new URI(authRequest.getIdentity()).getPath().equals(matchPath)) {
        if (isImmediate)
          return sendAuthFailure(authRequest);
        return doLoginRequest(m, authRequest, rawReq);
      }
      
      LocalUser user = localUserRepository.findOne(username);
      // By the time we get here, we know that the claimed ID association is
      // valid, but we don't know if the user has agreed to log in to the site.
      expireOldSiteApprovals();
      if (userSiteApprovalRepository.findByAuthorisingUserAndSiteEndpoint(
            user, authRequest.getReturnTo()).isEmpty()) {
        if (isImmediate)
          return sendAuthFailure(authRequest);
        return doApprovalRequest(m, authRequest, rawReq);
      }
      
      // The user is authenticated to the claimed ID, and we have a valid
      // approval for the site to authenticate them, so we can safely send a
      // redirect to the return URL...
      return "redirect:" +
        openIDManager.authResponse(authRequest, null, null, true)
        .getDestinationUrl(true);
    } catch (URISyntaxException e) {
      return raw(m, DirectError.createDirectError("Invalid claimed ID.").keyValueFormEncoding());
    } catch (Exception e) {
      return raw(m, DirectError.createDirectError("Internal error: " + e.toString()).keyValueFormEncoding());
    }
  }

  private void expireOldSiteApprovals() {
    userSiteApprovalRepository.deleteExpiredApprovals(
        new DateTime().minusSeconds(settingService.loadIntSetting(SettingServiceImpl.approvalDuration)).toDate());
  }
  
  private String sendAuthFailure(AuthRequest authRequest) {
    return "redirect:" +
        openIDManager.authResponse(authRequest, null, null, false)
        .getDestinationUrl(true);
  }
  
  private String doLoginRequest(Model m, AuthRequest authRequest, HttpServletRequest rawReq)
      throws Exception {
    int lastSlash;
    String identity = authRequest.getIdentity();
    if ((lastSlash = identity.lastIndexOf('/')) == -1)
      return raw(m, DirectError.createDirectError("Invalid claimed ID.").keyValueFormEncoding());
    String username = URLDecoder.decode(identity.substring(lastSlash + 1), "UTF-8");
    m.addAttribute("needUser", username);
    requestCache.saveRequest(rawReq, null);
    return "login";
  }
  
  private String doApprovalRequest(Model m, AuthRequest authRequest, HttpServletRequest rawReq) {
    m.addAttribute("site", authRequest.getReturnTo());
    rawReq.getSession().setAttribute("openid-pending-request", rawReq);
    return "approval";
  }
}
