package com.quocchung.dangbai.duandangbai.exception;


/**
 * Exception khi không tìm thấy resource
 * Ví dụ: User not found, Post not found, etc.
 */
public class ResourceNotFoundException extends BaseException {

  public ResourceNotFoundException(ErrorCode errorCode) {
    super(errorCode);
  }

  public ResourceNotFoundException(ErrorCode errorCode, String customMessage) {
    super(errorCode, customMessage);
  }

  // Constructor tiện lợi cho resource cụ thể
  public ResourceNotFoundException(String resourceName, Long id) {
    super(ErrorCode.NOT_FOUND, resourceName + " not found with id: " + id);
  }

  public ResourceNotFoundException(String resourceName, String field, Object value) {
    super(ErrorCode.NOT_FOUND,
        resourceName + " not found with " + field + ": " + value);
  }
}