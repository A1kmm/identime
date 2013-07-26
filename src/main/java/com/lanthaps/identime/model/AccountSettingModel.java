package com.lanthaps.identime.model;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;


/**
 * Represents the contents of the account settings form.
 * @author Andrew Miller
 */
public class AccountSettingModel {
  public AccountSettingModel() {}
  
  /**
   * Creates a new AccountSettingModel with the specified parameters.
   * @param token
   * @param currentPassword
   * @param newPassword
   * @param newPassword2
   * @param email
   */
  public AccountSettingModel(String token, String currentPassword,
      String newPassword, String newPassword2, String email) {
    this.token = token;
    this.currentPassword = currentPassword;
    this.newPassword = newPassword;
    this.newPassword2 = newPassword2;
    this.email = email;
  }

  /**
   * If non-null, the token to use to allow account setting access.
   * Either token or currentPassword is required.
   */
  String token;
  
  /**
   * The current password. Either token or currentPassword is required.
   */
  String currentPassword;
  
  /**
   * The new password. If left blank, don't change the password.
   */
  @Size(min=6,max=40) String newPassword;
  
  /**
   * The new password, repeated. Must match newPassword.
   */
  @Size(min=6,max=40) String newPassword2;

  /**
   * The e-mail. 
   */
  @Email String email;

  /**
   * @return the token
   */
  public String getToken() {
    return token;
  }

  /**
   * @param token the token to set
   */
  public void setToken(String token) {
    this.token = token;
  }

  /**
   * @return the currentPassword
   */
  public String getCurrentPassword() {
    return currentPassword;
  }

  /**
   * @param currentPassword the currentPassword to set
   */
  public void setCurrentPassword(String currentPassword) {
    this.currentPassword = currentPassword;
  }

  /**
   * @return the newPassword
   */
  public String getNewPassword() {
    return newPassword;
  }

  /**
   * @param newPassword the newPassword to set
   */
  public void setNewPassword(String newPassword) {
    this.newPassword = newPassword;
  }

  /**
   * @return the newPassword2
   */
  public String getNewPassword2() {
    return newPassword2;
  }

  /**
   * @param newPassword2 the newPassword2 to set
   */
  public void setNewPassword2(String newPassword2) {
    this.newPassword2 = newPassword2;
  }

  /**
   * @return the email
   */
  public String getEmail() {
    return email;
  }

  /**
   * @param email the email to set
   */
  public void setEmail(String email) {
    this.email = email;
  }
}
