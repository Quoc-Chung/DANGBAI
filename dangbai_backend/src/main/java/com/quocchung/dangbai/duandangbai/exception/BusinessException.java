package com.quocchung.dangbai.duandangbai.exception;
 /*
 * Exception cho các lỗi logic nghiệp vụ
 * Ví dụ: Email đã tồn tại, Không thể xóa bài đã duyệt, etc.
 */
public class BusinessException extends BaseException {

  public BusinessException(ErrorCode errorCode) {
    super(errorCode);
  }

  public BusinessException(ErrorCode errorCode, String customMessage) {
    super(errorCode, customMessage);
  }

  public BusinessException(ErrorCode errorCode, String customMessage, Throwable cause) {
    super(errorCode, customMessage, cause);
  }
}