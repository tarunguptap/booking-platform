package com.organization.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class DataBaseException extends RuntimeException {

  public DataBaseException(String message) {
    super(message);
  }

  public DataBaseException(String message, Throwable cause) {
    super(message, cause);
  }

  public DataBaseException(Throwable cause) {
    super(cause);
  }
}
