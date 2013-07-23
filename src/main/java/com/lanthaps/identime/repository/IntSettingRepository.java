package com.lanthaps.identime.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.lanthaps.identime.model.IntSetting;

@Repository
public interface IntSettingRepository extends CrudRepository<IntSetting, String> {
}
