package com.lanthaps.identime.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity @DiscriminatorValue("B")
public class BooleanSetting extends Setting {
  private boolean booleanValue;

  /**
   * @return the booleanValue
   */
  public boolean getBooleanValue() {
    return booleanValue;
  }

  /**
   * @param booleanValue the booleanValue to set
   */
  public void setBooleanValue(boolean booleanValue) {
    this.booleanValue = booleanValue;
  }

}
