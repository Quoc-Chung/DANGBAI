package com.quocchung.dangbai.duandangbai.exception;


/**
 * Exception cho các lỗi xác thực
 * Ví dụ: Sai username/password, Token hết hạn, etc.
 */
public class AuthenticationException extends BaseException {

  public AuthenticationException(ErrorCode errorCode) {
    super(errorCode);
  }

  public AuthenticationException(ErrorCode errorCode, String customMessage) {
    super(errorCode, customMessage);
  }

  public AuthenticationException(ErrorCode errorCode, String customMessage, Throwable cause) {
    super(errorCode, customMessage, cause);
  }
}