/**
 * 
 */
package com.lanthaps.identime.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.lanthaps.identime.model.LocalUser;
import com.lanthaps.identime.model.UserSiteApproval;

/**
 * Repository for UserSiteApproval entities.
 * @author Andrew Miller
 */
@Repository
public interface UserSiteApprovalRepository extends
    CrudRepository<UserSiteApproval, Integer> {
  @Query("DELETE FROM UserSiteApproval WHERE approvalTimestamp < :expireTime") @Modifying @Transactional
  void deleteExpiredApprovals(@Param("expireTime") Date expireTime);
  
  List<UserSiteApproval> findByAuthorisingUserAndSiteEndpoint(LocalUser authorisingUser, String siteEndpoint);
}
