package com.quocchung.dangbai.duandangbai.exception;

public class CategoryException extends  BaseException{

  public CategoryException(ErrorCode errorCode) {
    super(errorCode);
  }

  public CategoryException(ErrorCode errorCode, String customMessage) {
    super(errorCode, customMessage);
  }

  public CategoryException(ErrorCode errorCode, String customMessage, Throwable cause) {
    super(errorCode, customMessage, cause);
  }
}
