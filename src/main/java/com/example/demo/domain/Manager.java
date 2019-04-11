package com.example.demo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class Manager {

  private int id;
  private String firstName;
  private String middleName;
  private String lastName;
  private String phone;
  private Manager alternate;

  @JsonIgnore
  public String getFullName() {
    return firstName + " " + middleName + " " + lastName;
  }
}
