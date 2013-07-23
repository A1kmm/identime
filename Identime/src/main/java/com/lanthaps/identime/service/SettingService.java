package com.lanthaps.identime.service;

import com.lanthaps.identime.model.SettingInformation;

/**
 * A SettingService provides a way to retrieve and modify settings.
 * @author Andrew Miller
 */
public interface SettingService {
  /**
   * @return The set of all settings that can be set.
   */
  public SettingInformation<?>[] getAllSettings();
  
  public String loadStringSetting(SettingInformation<String> settingInfo);
  public boolean loadBooleanSetting(SettingInformation<Boolean> settingInfo);
  public int loadIntSetting(SettingInformation<Integer> settingInfo);
  
  public void saveStringSetting(SettingInformation<String> settingInfo, String value);
  public void saveBooleanSetting(SettingInformation<Boolean> settingInfo, boolean value);
  public void saveIntSetting(SettingInformation<Integer> settingInfo, int value);
}
