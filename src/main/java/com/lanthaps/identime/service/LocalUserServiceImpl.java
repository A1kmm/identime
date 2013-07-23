package com.lanthaps.identime.service;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanthaps.identime.model.LocalAuthority;
import com.lanthaps.identime.model.LocalUser;
import com.lanthaps.identime.repository.LocalAuthorityRepository;
import com.lanthaps.identime.repository.LocalUserRepository;

@Service @Transactional
public class LocalUserServiceImpl implements LocalUserService {
  @Autowired private LocalUserRepository localUserRepository;
  @Autowired private LocalAuthorityRepository localAuthorityRepository;
  
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
    logger.info("Passing user " + u.getUsername() + " with password " + u.getPassword());
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
}