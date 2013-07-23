package com.lanthaps.identime.model;

import javax.persistence.*;

/**
 * A LocalAuthority represents the permissions held by a user. 
 * @author Andrew Miller
 */
@Entity
public class LocalAuthority {
  /**
   * @return the id
   */
  public int getId() {
    return id;
  }

  /**
   * @param id the id to set
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * @return the user
   */
  public LocalUser getLocalUser() {
    return localUser;
  }

  /**
   * @param user the user to set
   */
  public void setLocalUser(LocalUser user) {
    this.localUser = user;
  }

  /**
   * @return the authority
   */
  public String getAuthority() {
    return authority;
  }

  /**
   * @param authority the authority to set
   */
  public void setAuthority(String authority) {
    this.authority = authority;
  }

  @Id @GeneratedValue(strategy=GenerationType.SEQUENCE)
  private int id;
  
  @ManyToOne
  private LocalUser localUser;
  
  private String authority;
}
