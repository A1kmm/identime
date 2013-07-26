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
  private StringSettingRepository stringSettings;
  private IntSettingRepository intSettings;
  private BooleanSettingRepository booleanSettings;
  
  @Autowired public void setStringSettings(StringSettingRepository stringSettings) {
    this.stringSettings = stringSettings;
  }

  @Autowired public void setIntSettings(IntSettingRepository intSettings) {
    this.intSettings = intSettings;
  }

  @Autowired public void setBooleanSettings(BooleanSettingRepository booleanSettings) {
    this.booleanSettings = booleanSettings;
  }

  public SettingServiceImpl() {};
  
  public static SettingInformation<String> baseURL =
      new SettingInformation<String>(String.class, "web.baseURL", "Base URL for the web application", ""),
      emailFrom =
      new SettingInformation<String>(String.class, "email.from", "From e-mail for e-mails sent out",
          "identime-admin@example.org"),
      emailServer =
          new SettingInformation<String>(String.class, "email.server", "E-mail server to send e-mails from",
              "smtp.example.org"),
      emailProtocol =
          new SettingInformation<String>(String.class, "email.protocol", "Protocol to use to send e-mail",
              "smtp"),
      emailUsername =
          new SettingInformation<String>(String.class, "email.username", "Username to use to send e-mail (or blank)",
              ""),
      emailPassword =
              new SettingInformation<String>(String.class, "email.password", "Password to use to send e-mail",
                  "");
  public static SettingInformation<Integer> emailPort =
      new SettingInformation<Integer>(Integer.class, "email.port", "Port to use to send e-mail",
      25),
      approvalDuration =
      new SettingInformation<Integer>(Integer.class, "openid.approvalDuration",
        "The time (in seconds) that OpenID site approvals last before expiring",
        3600 * 24 * 2),
      resetEmailDelayDuration =
        new SettingInformation<Integer>(Integer.class, "account.resetEmailDelayDuration",
        "The time (in seconds) after getting a reset password e-mail that another can be sent",
        3600 * 2),
      tokenExpiryTime =
        new SettingInformation<Integer>(Integer.class, "account.tokenExpiryTime",
            "The time (in seconds) that a reset password token is valid for",
            3600 * 24 * 2);
  
  private static final SettingInformation<?>[] allSettings = {
    baseURL, emailFrom, emailServer, emailProtocol, emailPort, emailUsername, emailPassword,
    approvalDuration, resetEmailDelayDuration, tokenExpiryTime };
  
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
