package com.lanthaps.identime.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.UserDetailsManager;

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
}
