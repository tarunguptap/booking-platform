package com.organization.exception;

public class EntityAlreadyExistsException extends RuntimeException {
  private String logMessage;

  public String getLogMessage() {
    return logMessage;
  }

  public EntityAlreadyExistsException(String message) {
    super(message);
  }

  public EntityAlreadyExistsException(String message, String logMessage) {
    super(message);
    this.logMessage = logMessage;
  }

  public EntityAlreadyExistsException(String message, Throwable cause) {
    super(message, cause);
  }

  public EntityAlreadyExistsException(Throwable cause) {
    super(cause);
  }
}
