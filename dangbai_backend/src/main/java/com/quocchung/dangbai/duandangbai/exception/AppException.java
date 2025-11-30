package com.quocchung.dangbai.duandangbai.exception;

public class AppException extends BaseException{

  public AppException(ErrorCode errorCode) {
    super(errorCode);
  }

  public AppException(ErrorCode errorCode, String customMessage) {
    super(errorCode, customMessage);
  }

  public AppException(ErrorCode errorCode, String customMessage, Throwable cause) {
    super(errorCode, customMessage, cause);
  }
}
