package com.quocchung.dangbai.duandangbai.exception;


import lombok.Getter;

/**
 * Exception cho lỗi rate limit
 * Ví dụ: Too many requests, Daily limit exceeded, etc.
 */
@Getter
public class RateLimitException extends BaseException {
  private final long retryAfterSeconds;

  public RateLimitException(ErrorCode errorCode, long retryAfterSeconds) {
    super(errorCode);
    this.retryAfterSeconds = retryAfterSeconds;
  }

  public RateLimitException(ErrorCode errorCode, String customMessage, long retryAfterSeconds) {
    super(errorCode, customMessage);
    this.retryAfterSeconds = retryAfterSeconds;
  }
}