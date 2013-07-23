package com.lanthaps.identime.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Index;

/**
 * A UserSiteApproval records that a user has  
 * @author Andrew Miller
 */
@Entity
public class UserSiteApproval {
  @Id @GeneratedValue(strategy=GenerationType.SEQUENCE) private Integer id;
  
  @ManyToOne private LocalUser authorisingUser;
  
  @Index(name="userAndEndpoint",columnNames={"authorisingUser", "siteEndpoint"})
  private String siteEndpoint;
  
  @Temporal(TemporalType.TIMESTAMP) @Index(name="byExpiry")
  private Date approvalTimestamp;
  
  /**
   * @return the id
   */
  public Integer getId() {
    return id;
  }
  /**
   * @param id the id to set
   */
  public void setId(Integer id) {
    this.id = id;
  }
  /**
   * @return the authorisingUser
   */
  public LocalUser getAuthorisingUser() {
    return authorisingUser;
  }
  /**
   * @param authorisingUser the authorisingUser to set
   */
  public void setAuthorisingUser(LocalUser authorisingUser) {
    this.authorisingUser = authorisingUser;
  }
  /**
   * @return the siteEndpoint
   */
  public String getSiteEndpoint() {
    return siteEndpoint;
  }
  /**
   * @param siteEndpoint the siteEndpoint to set
   */
  public void setSiteEndpoint(String siteEndpoint) {
    this.siteEndpoint = siteEndpoint;
  }
  /**
   * @return the approvalTimestamp
   */
  public Date getApprovalTimestamp() {
    return approvalTimestamp;
  }
  /**
   * @param approvalTimestamp the approvalTimestamp to set
   */
  public void setApprovalTimestamp(Date approvalTimestamp) {
    this.approvalTimestamp = approvalTimestamp;
  }
}
