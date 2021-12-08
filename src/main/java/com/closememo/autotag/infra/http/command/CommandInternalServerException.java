package com.closememo.autotag.infra.http.command;

import com.closememo.autotag.infra.exception.InternalServerException;

public class CommandInternalServerException extends InternalServerException {

  private static final long serialVersionUID = 1268618588534012446L;

  public CommandInternalServerException() {
  }

  public CommandInternalServerException(String message) {
    super(message);
  }
}
