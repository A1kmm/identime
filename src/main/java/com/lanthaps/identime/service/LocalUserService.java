package com.lanthaps.identime.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.UserDetailsManager;

import com.lanthaps.identime.exception.TokenValidityException;
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
   * already present. The e-mail is cleared if the e-mail is null. In any
   * case, any password reset token associated with an existing e-mail is cleared.
   * @param user
   * @param email
   */
  void setUserEmail(String user, String email);
  
  /**
   * Sends a password reset e-mail to an e-mail address associated with a user.
   */
  void sendPasswordReset(UserEmail userEmail);
  
  /**
   * Checks a token for validity.
   * @return The UserEmail corresponding to the token, if valid.
   * @throws TokenValidityException If the token is invalid.
   */
  UserEmail checkTokenValidity(String token) throws TokenValidityException;

  /**
   * Changes the password for a specified username.
   * @param username
   * @param password
   */
  void setUserPassword(String username, String password);
}
