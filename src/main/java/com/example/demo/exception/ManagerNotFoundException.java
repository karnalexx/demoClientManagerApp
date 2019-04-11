package com.example.demo.exception;

public class ManagerNotFoundException extends RuntimeException {

  public ManagerNotFoundException(int id) {
    super("Could not find manager with id " + id);
  }

}
