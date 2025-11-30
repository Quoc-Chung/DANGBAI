package com.quocchung.dangbai.duandangbai.exception;

/**
 * Exception cho các lỗi upload file
 * Ví dụ: File quá lớn, Format không hợp lệ, etc.
 */
public class FileUploadException extends BaseException {

  public FileUploadException(ErrorCode errorCode) {
    super(errorCode);
  }

  public FileUploadException(ErrorCode errorCode, String customMessage) {
    super(errorCode, customMessage);
  }

  public FileUploadException(ErrorCode errorCode, String fileName, String reason) {
    super(errorCode, "Failed to upload file '" + fileName + "': " + reason);
  }
}