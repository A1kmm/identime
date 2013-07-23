package com.lanthaps.identime.web;

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

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

import com.lanthaps.identime.model.RegisterModel;
import com.lanthaps.identime.service.CSRFTokenService;
import com.lanthaps.identime.service.LocalUserServiceImpl;

@Controller
public class AccountController {
  private static Logger logger = LoggerFactory.getLogger(AccountController.class);
  
  /**
   * Supplies a login form.
   */
  @RequestMapping(value = "/login", method = RequestMethod.GET)
  public String login(HttpSession ses) {
    return "login";
  }

  @Autowired private LocalUserServiceImpl localUserService;
  @Autowired private PasswordEncoder passwordEncoder;
  
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
    if (!CSRFTokenService.checkToken(req))
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
    
    // Autologin the new user...
    Authentication newAuth = new UsernamePasswordAuthenticationToken(newUser.getUsername(),
        registerModel.getPassword(), auths);
    SecurityContextHolder.getContext().setAuthentication(newAuth);
    
    return "redirect:./";
  }
}
