package com.lanthaps.identime.model;

/**
 * A SettingInformation provides information about a possible setting. It
 * helps to ensure that the list of valid settings remains documented in
 * this service. 
 *  
 * @author Andrew Miller
 * @param <T> The type of the setting.
 */
public class SettingInformation<T> {
  private Class<T> type;
  private String name, description;
  private T defaultValue;
  
  /**
   * This constructor is intended for use by SettingService implementations to
   * create a SettingInformation to use in the list of valid settings. 
   */
  public SettingInformation(Class<T> type, String name, String description, T defaultValue) {
    this.name = name;
    this.description = description;
    this.defaultValue = defaultValue;
    this.type = type;
  } 
      
  /**
   * @return the type
   */
  public Class<T> getType() {
    return type;
  }
  
  /**
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * @param name the name to set
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * @return the description
   */
  public String getDescription() {
    return description;
  }

  /**
   * @param description the description to set
   */
  public void setDescription(String description) {
    this.description = description;
  }
  
  /**
   * @return the defaultValue
   */
  public T getDefaultValue() {
    return defaultValue;
  }

  /**
   * @param defaultValue the defaultValue to set
   */
  public void setDefaultValue(T defaultValue) {
    this.defaultValue = defaultValue;
  }
};
