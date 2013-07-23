package com.lanthaps.identime.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.lanthaps.identime.model.StringSetting;

@Repository
public interface StringSettingRepository extends CrudRepository<StringSetting, String> {
}
