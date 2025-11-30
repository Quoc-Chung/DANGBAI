package com.quocchung.dangbai.duandangbai.exception;

/**
 * Exception cho các conflict
 * Ví dụ: Duplicate entry, Resource already exists, etc.
 */
public class ConflictException extends BaseException {

  public ConflictException(ErrorCode errorCode) {
    super(errorCode);
  }

  public ConflictException(ErrorCode errorCode, String customMessage) {
    super(errorCode, customMessage);
  }

  public ConflictException(String resourceName, String field, Object value) {
    super(ErrorCode.CONSTRAINT_VIOLATION,
        resourceName + " with " + field + " '" + value + "' already exists");
  }
}