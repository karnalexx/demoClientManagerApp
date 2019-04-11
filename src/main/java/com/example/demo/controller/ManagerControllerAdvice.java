package com.example.demo.controller;

import com.example.demo.exception.ManagerNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ManagerControllerAdvice {

  @ResponseBody
  @ExceptionHandler(ManagerNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  String managerNotFoundHandler(ManagerNotFoundException ex) {
    return ex.getMessage();
  }

}
