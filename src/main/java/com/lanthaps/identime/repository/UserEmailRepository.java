package com.lanthaps.identime.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.lanthaps.identime.model.UserEmail;

@Repository
public interface UserEmailRepository extends CrudRepository<UserEmail,String> {
  public UserEmail findByCurrentEmailResetToken(String token);
}
