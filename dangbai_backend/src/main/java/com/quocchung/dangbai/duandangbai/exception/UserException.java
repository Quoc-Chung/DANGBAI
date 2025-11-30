package com.quocchung.dangbai.duandangbai.exception;

public class UserException extends BaseException{

  public UserException(ErrorCode errorCode) {
    super(errorCode);
  }

  public UserException(ErrorCode errorCode, String customMessage) {
    super(errorCode, customMessage);
  }

  public UserException(ErrorCode errorCode, String customMessage, Throwable cause) {
    super(errorCode, customMessage, cause);
  }
}
