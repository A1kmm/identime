package com.lanthaps.identime.model;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * A LocalUser represents a user who can authenticate locally (if the account is enabled).
 * @author Andrew Miller
 */
@Entity
public class LocalUser {
  @Id private String username;
  
  private String password;
  private boolean enabled;
  
  /**
   * @return the username
   */
  public String getUsername() {
    return username;
  }
  /**
   * @param username the username to set
   */
  public void setUsername(String username) {
    this.username = username;
  }
  /**
   * @return the password
   */
  public String getPassword() {
    return password;
  }
  /**
   * @param password the password to set
   */
  public void setPassword(String password) {
    this.password = password;
  }
  /**
   * @return the enabled
   */
  public boolean isEnabled() {
    return enabled;
  }
  /**
   * @param enabled the enabled to set
   */
  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }
}
