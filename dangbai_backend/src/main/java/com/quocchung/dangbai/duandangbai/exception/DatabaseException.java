package com.quocchung.dangbai.duandangbai.exception;

/**
 * Exception cho các lỗi database
 * Ví dụ: Connection error, Transaction failed, etc.
 */
public class DatabaseException extends BaseException {

  public DatabaseException(ErrorCode errorCode) {
    super(errorCode);
  }

  public DatabaseException(ErrorCode errorCode, String customMessage) {
    super(errorCode, customMessage);
  }

  public DatabaseException(ErrorCode errorCode, Throwable cause) {
    super(errorCode, errorCode.getMessage(), cause);
  }

  public DatabaseException(ErrorCode errorCode, String customMessage, Throwable cause) {
    super(errorCode, customMessage, cause);
  }
}
