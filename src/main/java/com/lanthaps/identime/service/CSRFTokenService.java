package com.lanthaps.identime.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This service generates and checks tokens on forms to make sure that the form
 * is not a cross-site request forgery (CSRF). 
 * @author Andrew Miller
 */
@Service
public class CSRFTokenService {
  public static final String TOKEN_SESSION = "csrfToken";
  public static final String TOKEN_FORM = "csrfToken";
  
  private static Logger logger = LoggerFactory.getLogger(CSRFTokenService.class); 
  @Autowired private SecureTokenService secureTokenService;
  
  public String getSessionToken(HttpSession session) {
    logger.info("Getting session token, session = " + ((session==null)?"null":session.toString()));
    String token = (String)session.getAttribute(TOKEN_SESSION);
    if (token != null)
      return token;
    synchronized(session) {
      token = (String)session.getAttribute(TOKEN_SESSION);
      if (token != null)
        return token;
      String newToken = secureTokenService.makeToken();
      session.setAttribute(TOKEN_SESSION, newToken);
      return newToken;
    }
  }

  public boolean checkToken(HttpServletRequest req) {
    logger.debug("Comparing tokens " + getSessionToken(req.getSession()) + " and " +
        req.getParameter(TOKEN_FORM));
    return getSessionToken(req.getSession()).equals(req.getParameter(TOKEN_FORM));
  }
}
