package com.lanthaps.identime.service;

import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * A MailServerConfigurator applies settings to a JavaMailSender.
 * @author Andrew Miller
 */
public interface MailServerConfigurator {
  /**
   * Ensure that the mailSender has the correct configuration, changing the
   * configuration where it doesn't match the selected configuration.
   * @param mailSender
   */
  public void ensureServerConfiguration(JavaMailSenderImpl mailSender);
}
