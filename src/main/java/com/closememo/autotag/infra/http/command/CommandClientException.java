package com.closememo.autotag.infra.http.command;

import com.closememo.autotag.infra.exception.InternalServerException;

public class CommandClientException extends InternalServerException {

  private static final long serialVersionUID = 659552373967820559L;

  public CommandClientException() {
  }

  public CommandClientException(String message) {
    super(message);
  }
}
