package com.quocchung.dangbai.duandangbai.exception;


/**
 * Exception cho các lỗi từ service bên ngoài
 * Ví dụ: Email service, SMS service, Payment gateway, etc.
 */
public class ExternalServiceException extends BaseException {

  public ExternalServiceException(ErrorCode errorCode) {
    super(errorCode);
  }

  public ExternalServiceException(ErrorCode errorCode, String serviceName) {
    super(errorCode, serviceName + " service is currently unavailable");
  }

  public ExternalServiceException(ErrorCode errorCode, String customMessage, Throwable cause) {
    super(errorCode, customMessage, cause);
  }
}