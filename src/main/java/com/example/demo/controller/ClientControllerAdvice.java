package com.example.demo.controller;

import com.example.demo.exception.ClientNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ClientControllerAdvice {

  @ResponseBody
  @ExceptionHandler(ClientNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  String clientNotFoundHandler(ClientNotFoundException ex) {
    return ex.getMessage();
  }

}
