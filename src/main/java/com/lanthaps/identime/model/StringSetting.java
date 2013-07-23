package com.lanthaps.identime.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity @DiscriminatorValue("S")
public class StringSetting extends Setting {
  private String stringValue;

  /**
   * @return the stringValue
   */
  public String getStringValue() {
    return stringValue;
  }

  /**
   * @param stringValue the stringValue to set
   */
  public void setStringValue(String stringValue) {
    this.stringValue = stringValue;
  }
  
}
