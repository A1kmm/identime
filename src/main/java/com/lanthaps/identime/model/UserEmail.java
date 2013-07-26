package com.lanthaps.identime.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Index;

/**
 * UserEmail links an e-mail address to a user.
 * @author Andrew Miller
 */
@Entity
public class UserEmail implements Serializable {
  private static final long serialVersionUID = -3737729855950930288L;
  
  /**
   * The username of the user.
   */
  @NotNull @Id private String username;
  
  /**
   * A mapping to the localUser (by username).
   */
  @NotNull @OneToOne @MapsId private LocalUser localUser;
  
  /**
   * The e-mail address of the user.
   */
  @NotNull private String email;
  
  /**
   * If non-null, the date a reset was last sent. 
   */
  @Temporal(TemporalType.TIMESTAMP) private Date lastResetSent;
  
  /**
   * The most recent unused password reset token. 
   */
  @Index(name="reset_index") private String currentEmailResetToken;
  
  /**
   * The date on which the reset password token was issued.
   */
  @Temporal(TemporalType.TIMESTAMP) private Date tokenIssued;
  
  public String getUsername() {
    return username;
  }
  public void setUsername(String username) {
    this.username = username;
  }
  public LocalUser getLocalUser() {
    return localUser;
  }
  public void setLocalUser(LocalUser localUser) {
    this.localUser = localUser;
  }
  public String getEmail() {
    return email;
  }
  public void setEmail(String email) {
    this.email = email;
  }
  public Date getLastResetSent() {
    return lastResetSent;
  }
  public void setLastResetSent(Date lastResetSent) {
    this.lastResetSent = lastResetSent;
  }
  public String getCurrentEmailResetToken() {
    return currentEmailResetToken;
  }
  public void setCurrentEmailResetToken(String currentEmailResetToken) {
    this.currentEmailResetToken = currentEmailResetToken;
  }
  public Date getTokenIssued() {
    return tokenIssued;
  }
  public void setTokenIssued(Date tokenIssued) {
    this.tokenIssued = tokenIssued;
  }
}
