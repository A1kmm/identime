package com.lanthaps.identime.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanthaps.identime.model.*;
import com.lanthaps.identime.repository.*;

/**
 * A SettingService provides a way to retrieve and modify settings.
 * @author Andrew Miller
 */
@Transactional @Service
public class SettingServiceImpl implements SettingService {
  @Autowired private StringSettingRepository stringSettings;
  @Autowired private IntSettingRepository intSettings;
  @Autowired private BooleanSettingRepository booleanSettings;
  
  public SettingServiceImpl() {};
  
  public static SettingInformation<String> baseURL =
      new SettingInformation<String>(String.class, "web.baseURL", "Base URL for the web application", "");
  public static SettingInformation<Integer> approvalDuration =
      new SettingInformation<Integer>(Integer.class, "openid.approvalDuration",
          "The time (in seconds) that OpenID site approvals last before expiring",
          3600 * 24 * 2);
  
  private static final SettingInformation<?>[] allSettings = { baseURL, approvalDuration };
  
  /**
   * @return The set of all settings that can be set.
   */
  public SettingInformation<?>[] getAllSettings() {
    return allSettings;
  }
  
  public String loadStringSetting(SettingInformation<String> settingInfo) {
    StringSetting s = stringSettings.findOne(settingInfo.getName());
    if (s == null)
      return settingInfo.getDefaultValue();
    return s.getStringValue();
  }
  public boolean loadBooleanSetting(SettingInformation<Boolean> settingInfo) {
    BooleanSetting s = booleanSettings.findOne(settingInfo.getName());
    if (s == null)
      return settingInfo.getDefaultValue();
    return s.getBooleanValue();
  }
  public int loadIntSetting(SettingInformation<Integer> settingInfo) {
    IntSetting s = intSettings.findOne(settingInfo.getName());
    if (s == null)
      return settingInfo.getDefaultValue();
    return s.getIntValue();
  }
  
  public void saveStringSetting(SettingInformation<String> settingInfo, String value) {
    StringSetting s = new StringSetting();
    s.setName(settingInfo.getName());
    s.setStringValue(value);
    stringSettings.save(s);
  }
  public void saveBooleanSetting(SettingInformation<Boolean> settingInfo, boolean value) {
    BooleanSetting s = new BooleanSetting();
    s.setName(settingInfo.getName());
    s.setBooleanValue(value);
    booleanSettings.save(s);
  }
  public void saveIntSetting(SettingInformation<Integer> settingInfo, int value) {
    IntSetting s = new IntSetting();
    s.setName(settingInfo.getName());
    s.setIntValue(value);
    intSettings.save(s);
  }
}
