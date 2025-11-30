package com.quocchung.dangbai.duandangbai.exception;

/**
 * Exception cho các lỗi phân quyền
 * Ví dụ: Không có quyền truy cập, Không đủ permission, etc.
 */
public class AuthorizationException extends BaseException {

  public AuthorizationException(ErrorCode errorCode) {
    super(errorCode);
  }

  public AuthorizationException(ErrorCode errorCode, String customMessage) {
    super(errorCode, customMessage);
  }

  public AuthorizationException(String requiredPermission) {
    super(ErrorCode.INSUFFICIENT_PERMISSIONS,
        "Required permission: " + requiredPermission);
  }
}