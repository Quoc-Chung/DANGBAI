package com.quocchung.dangbai.duandangbai.exception;


import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

/**
 * Exception cho lỗi validation
 * Chứa map các field và error message
 */
@Getter
public class ValidationException extends BaseException {
  private final Map<String, String> errors;

  public ValidationException(Map<String, String> errors) {
    super(ErrorCode.VALIDATION_ERROR, "Validation failed");
    this.errors = errors;
  }

  public ValidationException(String field, String message) {
    super(ErrorCode.VALIDATION_ERROR, "Validation failed");
    this.errors = new HashMap<>();
    this.errors.put(field, message);
  }

  public ValidationException(ErrorCode errorCode, Map<String, String> errors) {
    super(errorCode, "Validation failed");
    this.errors = errors;
  }
}