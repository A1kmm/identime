package com.lanthaps.identime.service;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityNotFoundException;

import org.apache.velocity.app.VelocityEngine;
import org.joda.time.DateTime;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.lanthaps.identime.exception.TokenValidityException;
import com.lanthaps.identime.model.LocalAuthority;
import com.lanthaps.identime.model.LocalUser;
import com.lanthaps.identime.model.UserEmail;
import com.lanthaps.identime.repository.LocalAuthorityRepository;
import com.lanthaps.identime.repository.LocalUserRepository;
import com.lanthaps.identime.repository.UserEmailRepository;

import java.util.HashMap;
import java.util.Map;

@Service @Transactional
public class LocalUserServiceImpl implements LocalUserService {
  private LocalUserRepository localUserRepository;
  private LocalAuthorityRepository localAuthorityRepository;
  private UserEmailRepository userEmailRepository;
  private VelocityEngine velocityEngine;
  private JavaMailSenderImpl mailSender;
  private SecureTokenService secureTokenService;
  private SettingService settingService;
  private MailServerConfigurator mailServiceConfigurator;
  private PasswordEncoder passwordEncoder;
  
  @Autowired public void setLocalUserRepository(LocalUserRepository localUserRepository) {
    this.localUserRepository = localUserRepository;
  }

  @Autowired public void setLocalAuthorityRepository(
      LocalAuthorityRepository localAuthorityRepository) {
    this.localAuthorityRepository = localAuthorityRepository;
  }

  @Autowired public void setUserEmailRepository(UserEmailRepository userEmailRepository) {
    this.userEmailRepository = userEmailRepository;
  }

  @Autowired public void setVelocityEngine(VelocityEngine velocityEngine) {
    this.velocityEngine = velocityEngine;
  }

  @Autowired public void setMailSender(JavaMailSenderImpl mailSender) {
    this.mailSender = mailSender;
  }

  @Autowired public void setSecureTokenService(SecureTokenService secureTokenService) {
    this.secureTokenService = secureTokenService;
  }

  @Autowired public void setSettingService(SettingService settingService) {
    this.settingService = settingService;
  }

  @Autowired public void setMailServiceConfigurator(
      MailServerConfigurator mailServiceConfigurator) {
    this.mailServiceConfigurator = mailServiceConfigurator;
  }

  @Autowired public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
    this.passwordEncoder = passwordEncoder;
  }

  private static Logger logger = LoggerFactory.getLogger(LocalUserServiceImpl.class);
  
  public LocalUserServiceImpl() {
    isNew = true;
  }
  
  /* (non-Javadoc)
   * @see org.springframework.security.provisioning.UserDetailsManager#createUser(org.springframework.security.core.userdetails.UserDetails)
   */
  @Override
  public void createUser(UserDetails user) {
    LocalUser u = new LocalUser();
    u.setUsername(user.getUsername());
    u.setPassword(user.getPassword());
    u.setEnabled(user.isEnabled());
    localUserRepository.save(u);
    matchAuthorities(u, user.getAuthorities());
  }

  /* (non-Javadoc)
   * @see org.springframework.security.provisioning.UserDetailsManager#updateUser(org.springframework.security.core.userdetails.UserDetails)
   */
  @Override
  public void updateUser(UserDetails user) {
    LocalUser u = localUserRepository.findOne(user.getUsername());
    u.setUsername(user.getUsername());
    u.setPassword(user.getPassword());
    u.setEnabled(user.isEnabled());
    localUserRepository.save(u);
    matchAuthorities(u, user.getAuthorities());
  }

  private void matchAuthorities(LocalUser user, Collection<? extends GrantedAuthority> auths) {
    try {
      localAuthorityRepository.delete(localAuthorityRepository.findByLocalUser(user));
    } catch (EntityNotFoundException e) {}
    for (GrantedAuthority gAuth : auths) {
      LocalAuthority localAuth = new LocalAuthority();
      localAuth.setLocalUser(user);
      localAuth.setAuthority(gAuth.getAuthority());
      localAuthorityRepository.save(localAuth);
    }
  }
  
  /* (non-Javadoc)
   * @see org.springframework.security.provisioning.UserDetailsManager#deleteUser(java.lang.String)
   */
  @Override
  public void deleteUser(String username) {
    localUserRepository.delete(username);
  }

  /* (non-Javadoc)
   * @see org.springframework.security.provisioning.UserDetailsManager#changePassword(java.lang.String, java.lang.String)
   */
  @Override
  public void changePassword(String oldPassword, String newPassword) {
    LocalUser u =
        localUserRepository.findOne(SecurityContextHolder.getContext().getAuthentication().getName());
    u.setPassword(newPassword);
  }

  /* (non-Javadoc)
   * @see org.springframework.security.provisioning.UserDetailsManager#userExists(java.lang.String)
   */
  @Override
  public boolean userExists(String username) {
    return localUserRepository.exists(username);
  }

  /* (non-Javadoc)
   * @see org.springframework.security.core.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
   */
  @Override
  public UserDetails loadUserByUsername(String username)
      throws UsernameNotFoundException {
    logger.info("Looking up user " + username);
    LocalUser localUser = localUserRepository.findOne(username);
    List<LocalAuthority> allLocalAuths = localAuthorityRepository.findByLocalUser(localUser);
    List<GrantedAuthority> auths = new LinkedList<GrantedAuthority>();
    for (LocalAuthority localAuth : allLocalAuths)
      auths.add(new SimpleGrantedAuthority(localAuth.getAuthority()));
    UserDetails u = new User(localUser.getUsername(), localUser.getPassword(),
        localUser.isEnabled(), true, true, true,
        auths);
    return u;
  }

  /**
   * Set to false when we have confirmed there is at least one user.
   */
  private static boolean isNew;
  
  @Override
  public boolean isNextUserFirst() {
    // Use the isNew cache to reduce database lookups.
    if (!isNew)
      return false;
    isNew = localUserRepository.count() == 0;
    return isNew;
  }

  @Override
  public void setUserEmail(String username, String email) {
    UserEmail userEmail = userEmailRepository.findOne(username);
    if (email != null && email.equals(""))
      email = null;
    if (userEmail == null) {
      if (email == null)
        return;
      userEmail = new UserEmail();
      userEmail.setUsername(username);
      userEmail.setLocalUser(localUserRepository.findOne(username));
    }
    if (email == null) {
      userEmailRepository.delete(userEmail);
      return;
    }
    userEmail.setEmail(email);
    userEmail.setCurrentEmailResetToken(null);
    userEmailRepository.save(userEmail);
  }

  @Override
  public void sendPasswordReset(final UserEmail ue) {
    ue.setLastResetSent(new Date());
    final String token = secureTokenService.makeToken();
    ue.setCurrentEmailResetToken(token);
    ue.setTokenIssued(new Date());
    
    userEmailRepository.save(ue);

    mailServiceConfigurator.ensureServerConfiguration(mailSender);
    mailSender.send(new MimeMessagePreparator() {
      @Override public void prepare(MimeMessage msg) throws MessagingException {
        MimeMessageHelper message = new MimeMessageHelper(msg);
        message.setTo(ue.getEmail());
        message.setSubject("Password reset");
        message.setFrom(settingService.loadStringSetting(SettingServiceImpl.emailFrom));
        
        Map<String,Object> mailModel = new HashMap<String,Object>();
        
        mailModel.put("token", token);
        mailModel.put("baseURL", settingService.loadStringSetting(SettingServiceImpl.baseURL));
        String mailMsg =
            VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "reset-password.vm",
                "UTF-8", mailModel);
        
        message.setText(mailMsg, false);
      }
    });
  }

  @Override
  public UserEmail checkTokenValidity(String token)
      throws TokenValidityException {
    if (token == null || token.equals(""))
      throw new TokenValidityException("No token provided");
    UserEmail ue = userEmailRepository.findByCurrentEmailResetToken(token);
    if (ue == null)
      throw new TokenValidityException("Password reset token not found; it may have " +
          "already been used. Try sending a new reset e-mail.");

    if (new DateTime(ue.getTokenIssued()).plusSeconds(
            settingService.loadIntSetting(SettingServiceImpl.tokenExpiryTime)).
            isBeforeNow())
      throw new TokenValidityException("Password reset token has expired. " +
            "Try sending a new reset e-mail.");

    return ue;
  }

  @Override
  public void setUserPassword(String username, String password) {
    LocalUser u = localUserRepository.findOne(username);
    u.setPassword(passwordEncoder.encode(password));
    localUserRepository.save(u);
  }
}