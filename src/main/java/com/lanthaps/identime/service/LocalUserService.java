package com.lanthaps.identime.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.UserDetailsManager;

import com.lanthaps.identime.model.UserEmail;

/**
 * This service provides information about the local user accounts. 
 * @author Andrew Miller
 */
public interface LocalUserService extends UserDetailsService, UserDetailsManager {
  /**
   * Checks to see if there are no users already (so that the first user to be
   * created can be given special authorities).
   * @return true if there are no users yet.
   */
  boolean isNextUserFirst();
  
  /**
   * Sets a new e-mail for a user, or replaces the existing e-mail if it is
   * already present.
   * @param user
   * @param email
   */
  void setUserEmail(User user, String email);
  
  /**
   * Sends a password reset e-mail to an e-mail address associated with a user.
   */
  void sendPasswordReset(UserEmail userEmail);
}
