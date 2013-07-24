package com.lanthaps.identime.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

@Service
public class MailServerConfiguratorImpl implements MailServerConfigurator {
  @Autowired private SettingService settingService;
  
  @Override
  public void ensureServerConfiguration(JavaMailSenderImpl mailSender) {
    mailSender.setHost(settingService.loadStringSetting(SettingServiceImpl.emailServer));
    String username = settingService.loadStringSetting(SettingServiceImpl.emailUsername);
    if (username == "")
      username = null;
    mailSender.setUsername(username);
    String password = settingService.loadStringSetting(SettingServiceImpl.emailPassword);
    if (password == "")
      password = null;
    mailSender.setPassword(password);
    mailSender.setPort(settingService.loadIntSetting(SettingServiceImpl.emailPort));
    mailSender.setProtocol(settingService.loadStringSetting(SettingServiceImpl.emailProtocol));
  }

}
