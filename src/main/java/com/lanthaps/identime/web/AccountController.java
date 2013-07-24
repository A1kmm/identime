package com.lanthaps.identime.web;

import java.util.Date;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormat;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.lanthaps.identime.model.RegisterModel;
import com.lanthaps.identime.model.UserEmail;
import com.lanthaps.identime.repository.UserEmailRepository;
import com.lanthaps.identime.service.CSRFTokenService;
import com.lanthaps.identime.service.LocalUserService;
import com.lanthaps.identime.service.SettingService;
import com.lanthaps.identime.service.SettingServiceImpl;

@Controller
public class AccountController {
  private static Logger logger = LoggerFactory.getLogger(AccountController.class);
  
  private UserEmailRepository userEmailRepository;
  private CSRFTokenService csrfTokenService;
  private LocalUserService localUserService;
  private PasswordEncoder passwordEncoder;
  private SettingService settingService;

  @Autowired public void setUserEmailRepository(UserEmailRepository userEmailRepository) {
    this.userEmailRepository = userEmailRepository;
  }

  @Autowired public void setCsrfTokenService(CSRFTokenService csrfTokenService) {
    this.csrfTokenService = csrfTokenService;
  }

  @Autowired public void setLocalUserService(LocalUserService localUserService) {
    this.localUserService = localUserService;
  }

  @Autowired public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
    this.passwordEncoder = passwordEncoder;
  }

  @Autowired public void setSettingService(SettingService settingService) {
    this.settingService = settingService;
  }

  /**
   * Supplies a login form.
   */
  @RequestMapping(value = "/login", method = RequestMethod.GET)
  public String login(HttpSession ses) {
    return "login";
  }
  
  /**
   * Supplies a register form.
   */
  @RequestMapping(value = "/register", method = RequestMethod.GET)
  public String registerQuery(Model model) {
    model.addAttribute("registerModel", new RegisterModel());
    return "register";
  }

  @RequestMapping(value = "/register", method = RequestMethod.POST)
  public String registerAttempt(
      HttpServletRequest req,
      @Valid @ModelAttribute RegisterModel registerModel,
      BindingResult bindingResult) {
    if (!csrfTokenService.checkToken(req))
      return "login";
    if (!registerModel.getPassword().equals(registerModel.getPassword2()))
      bindingResult.addError(new FieldError("registerModel", "password2", "Passwords don't match"));
    if (bindingResult.hasErrors())
      return "register";
    
    if (localUserService.userExists(registerModel.getUsername())) {
      logger.info("Attempt to register duplicate username " + registerModel.getUsername());
      bindingResult.addError(new FieldError("registerModel", "username", "Username already taken"));
      return "register";
    }

    // Create the new user...
    Vector<GrantedAuthority> auths = new Vector<GrantedAuthority>();
    if (localUserService.isNextUserFirst()) {
      logger.info("First user registration, granting admin privileges for " + registerModel.getUsername());
      auths.add(new SimpleGrantedAuthority("firstuser"));
      auths.add(new SimpleGrantedAuthority("changeauthorities"));
      auths.add(new SimpleGrantedAuthority("adminpanel"));
    }
    User newUser = new User(registerModel.getUsername(),
        passwordEncoder.encode(registerModel.getPassword()), auths);
    logger.info("Creating a new user " + registerModel.getUsername());
    localUserService.createUser(newUser);
    
    // If the e-mail is present, associate it with the account.
    if (registerModel.getEmail() != null && registerModel.getEmail() != "") {
      localUserService.setUserEmail(newUser, registerModel.getEmail());
    }
    
    // Autologin the new user...
    Authentication newAuth = new UsernamePasswordAuthenticationToken(newUser.getUsername(),
        registerModel.getPassword(), auths);
    SecurityContextHolder.getContext().setAuthentication(newAuth);
    
    return "redirect:./";
  }
  
  @RequestMapping(value = "/forgot", method = RequestMethod.GET)
  public String forgotPassword(Model m) {
    return "forgot";
  }
  
  @RequestMapping(value = "/forgot", method = RequestMethod.POST)
  public String doForgotPassword(@RequestParam String accountName,
      HttpServletRequest req, Model m) {
    if (csrfTokenService.checkToken(req))
      return "login";
    if (accountName == null || accountName == "") {
      m.addAttribute("error", "Please enter an account name to reset");
      return "forgot";
    }
    UserEmail ue = userEmailRepository.findOne(accountName);
    if (ue == null) {
      m.addAttribute("error", "No e-mail on record for that username");
      return "forgot";
    }
    Date d = ue.getLastResetSent();
    if (d != null) {
      DateTime whenCanReset = new DateTime(d).plusSeconds(settingService.loadIntSetting(
          SettingServiceImpl.resetEmailDelayDuration));
      if (whenCanReset.isAfterNow()) {
        Period canResetIn = new Period(new DateTime(), whenCanReset);
        m.addAttribute("error", "Sorry, you have used the forgot password feature " +
            "too recently. You can resend your forgot password e-mail in " +
            PeriodFormat.wordBased().print(canResetIn));
        return "forgot";
      }
    }
    
    localUserService.sendPasswordReset(ue);
    return "resetsent";
  }
}
