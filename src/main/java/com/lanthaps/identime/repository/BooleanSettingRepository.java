package com.lanthaps.identime.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.lanthaps.identime.model.BooleanSetting;

@Repository
public interface BooleanSettingRepository extends CrudRepository<BooleanSetting, String> {
}
