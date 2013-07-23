package com.lanthaps.identime.model;

import javax.persistence.*;

/**
 * The base class for all types of Setting.
 * @author Andrew Miller
 *
 */
@Entity @Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="type", discriminatorType=DiscriminatorType.STRING)
public class Setting {
  @Id protected String name;

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
}
