package com.lanthaps.identime.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity @DiscriminatorValue("I")
public class IntSetting extends Setting {
  private int intValue;

  /**
   * @return the intValue
   */
  public int getIntValue() {
    return intValue;
  }

  /**
   * @param intValue the intValue to set
   */
  public void setIntValue(int intValue) {
    this.intValue = intValue;
  }

}
