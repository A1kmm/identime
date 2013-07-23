package com.lanthaps.identime.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.lanthaps.identime.model.LocalAuthority;
import com.lanthaps.identime.model.LocalUser;

@Repository
public interface LocalAuthorityRepository extends
    CrudRepository<LocalAuthority, Integer> {
  List<LocalAuthority> findByLocalUser(LocalUser user);
}
