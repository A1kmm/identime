package com.lanthaps.identime.model;

import javax.validation.constraints.*;

import org.hibernate.validator.constraints.Email;

/**
 * The data supplied when a user registers on the site. 
 * @author Andrew Miller
 */
public class RegisterModel {
  @NotNull @Size(min=2,max=20)
  private String username;
  @NotNull @Size(min=6,max=40)
  private String password;
  private String password2;
  @Email
  private String email;
  
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
   * @return the password2
   */
  public String getPassword2() {
    return password2;
  }

  /**
   * @param password2 the password2 to set
   */
  public void setPassword2(String password2) {
    this.password2 = password2;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  } 
}
