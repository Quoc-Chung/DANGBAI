package com.quocchung.dangbai.duandangbai.exception;

import lombok.Getter;

@Getter
public abstract class BaseException extends RuntimeException {
  private final ErrorCode errorCode;
  private final String customMessage;

  public BaseException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
    this.customMessage = null;
  }

  public BaseException(ErrorCode errorCode, String customMessage) {
    super(customMessage);
    this.errorCode = errorCode;
    this.customMessage = customMessage;
  }

  public BaseException(ErrorCode errorCode, String customMessage, Throwable cause) {
    super(customMessage, cause);
    this.errorCode = errorCode;
    this.customMessage = customMessage;
  }

  public String getFinalMessage() {
    return customMessage != null ? customMessage : errorCode.getMessage();
  }
}