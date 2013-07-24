package com.lanthaps.identime.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * UserEmail links an e-mail address to a user.
 * @author Andrew Miller
 */
@Entity
public class UserEmail implements Serializable {
  private static final long serialVersionUID = -3737729855950930288L;
  
  @Id private String username;
  @OneToOne @MapsId private LocalUser localUser;
  private String email;
  @Temporal(TemporalType.TIMESTAMP) private Date lastResetSent;
  private String currentEmailResetToken;
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
}
