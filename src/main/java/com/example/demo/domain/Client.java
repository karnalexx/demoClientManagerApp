package com.example.demo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class Client {

  private int id;
  private String name;
  private String address;
  private Manager manager;
  @JsonIgnore private boolean deleted;

}
