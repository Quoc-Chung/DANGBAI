package com.quocchung.dangbai.duandangbai.dtos.response;

import com.quocchung.dangbai.duandangbai.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {

  private int code;
  private int status;
  private String message;
  private T data;
  private Long timestamp;
  private String path;


  public static <T> ApiResponse<T> success(T data) {
    return ApiResponse.<T>builder()
        .code(0)
        .status(HttpStatus.OK.value())
        .message("Success")
        .data(data)
        .timestamp(System.currentTimeMillis())
        .build();
  }

  public static <T> ApiResponse<T> success(String message, T data) {
    return ApiResponse.<T>builder()
        .code(0)
        .status(HttpStatus.OK.value())
        .message(message)
        .data(data)
        .timestamp(System.currentTimeMillis())
        .build();
  }

  public static <T> ApiResponse<T> successWithMessage(String message) {
    return ApiResponse.<T>builder()
        .code(0)
        .status(HttpStatus.OK.value())
        .message(message)
        .timestamp(System.currentTimeMillis())
        .build();
  }

  public static <T> ApiResponse<T> success() {
    return ApiResponse.<T>builder()
        .code(0)
        .status(HttpStatus.OK.value())
        .message("Success")
        .timestamp(System.currentTimeMillis())
        .build();
  }

  public static <T> ApiResponse<T> successWithStatus(int status, String message, T data) {
    return ApiResponse.<T>builder()
        .code(0)
        .status(status)
        .message(message)
        .data(data)
        .timestamp(System.currentTimeMillis())
        .build();
  }

  public static <T> ApiResponse<T> successWithStatus(int status, String message) {
    return ApiResponse.<T>builder()
        .code(0)
        .status(status)
        .message(message)
        .timestamp(System.currentTimeMillis())
        .build();
  }

  // ==================== ERROR METHODS ====================

  // Error với ErrorCode và path
  public static <T> ApiResponse<T> errorWithPath(ErrorCode errorCode, String path) {
    return ApiResponse.<T>builder()
        .code(errorCode.getCode())
        .status(errorCode.getStatus())
        .message(errorCode.getMessage())
        .timestamp(System.currentTimeMillis())
        .path(path)
        .build();
  }

  // Error với ErrorCode, không path
  public static <T> ApiResponse<T> error(ErrorCode errorCode) {
    return ApiResponse.<T>builder()
        .code(errorCode.getCode())
        .status(errorCode.getStatus())
        .message(errorCode.getMessage())
        .timestamp(System.currentTimeMillis())
        .build();
  }

  // Error với ErrorCode và custom message (không path)
  public static <T> ApiResponse<T> errorWithMessage(ErrorCode errorCode, String customMessage) {
    return ApiResponse.<T>builder()
        .code(errorCode.getCode())
        .status(errorCode.getStatus())
        .message(customMessage)
        .timestamp(System.currentTimeMillis())
        .build();
  }

  // Error với ErrorCode, custom message và path
  public static <T> ApiResponse<T> errorWithMessageAndPath(
      ErrorCode errorCode, String customMessage, String path) {
    return ApiResponse.<T>builder()
        .code(errorCode.getCode())
        .status(errorCode.getStatus())
        .message(customMessage)
        .timestamp(System.currentTimeMillis())
        .path(path)
        .build();
  }

  // Error với ErrorCode, data (validation) và path
  public static <T> ApiResponse<T> errorWithData(
      ErrorCode errorCode, T data, String path) {
    return ApiResponse.<T>builder()
        .code(errorCode.getCode())
        .status(errorCode.getStatus())
        .message(errorCode.getMessage())
        .data(data)
        .timestamp(System.currentTimeMillis())
        .path(path)
        .build();
  }

  // Error với ErrorCode, custom message, data và path
  public static <T> ApiResponse<T> errorWithMessageAndData(
      ErrorCode errorCode, String customMessage, T data, String path) {
    return ApiResponse.<T>builder()
        .code(errorCode.getCode())
        .status(errorCode.getStatus())
        .message(customMessage)
        .data(data)
        .timestamp(System.currentTimeMillis())
        .path(path)
        .build();
  }

  // Error với HTTP status và message (không ErrorCode)
  public static <T> ApiResponse<T> errorWithHttpStatus(int status, String message) {
    return ApiResponse.<T>builder()
        .code(status)
        .status(status)
        .message(message)
        .timestamp(System.currentTimeMillis())
        .build();
  }

  // Error với HTTP status, message và path
  public static <T> ApiResponse<T> errorWithHttpStatusAndPath(
      int status, String message, String path) {
    return ApiResponse.<T>builder()
        .code(status)
        .status(status)
        .message(message)
        .timestamp(System.currentTimeMillis())
        .path(path)
        .build();
  }

  // Error đầy đủ tham số (dùng cho debug)
  public static <T> ApiResponse<T> errorFull(
      int code, int status, String message, T data, String path) {
    return ApiResponse.<T>builder()
        .code(code)
        .status(status)
        .message(message)
        .data(data)
        .timestamp(System.currentTimeMillis())
        .path(path)
        .build();
  }
  //============================= Page Response  ====================================

  /**
   * Success với PageResponse - Message mặc định
   *
   * @param pageResponse - PageResponse data
   * @return ApiResponse wrap PageResponse
   */
  public static <T> ApiResponse<PageResponse<T>> successPage(PageResponse<T> pageResponse) {
    return ApiResponse.<PageResponse<T>>builder()
        .code(0)
        .status(HttpStatus.OK.value())
        .message("Success")
        .data(pageResponse)
        .timestamp(System.currentTimeMillis())
        .build();
  }

  /**
   * Success với PageResponse - Custom message
   *
   * @param message      - Custom message
   * @param pageResponse - PageResponse data
   * @return ApiResponse wrap PageResponse
   */
  public static <T> ApiResponse<PageResponse<T>> successPage(
      String message, PageResponse<T> pageResponse) {
    return ApiResponse.<PageResponse<T>>builder()
        .code(0)
        .status(HttpStatus.OK.value())
        .message(message)
        .data(pageResponse)
        .timestamp(System.currentTimeMillis())
        .build();
  }

  /**
   * Success với PageResponse - Custom status và message
   *
   * @param status       - HTTP status code
   * @param message      - Custom message
   * @param pageResponse - PageResponse data
   * @return ApiResponse wrap PageResponse
   */
  public static <T> ApiResponse<PageResponse<T>> successPageWithStatus(
      int status, String message, PageResponse<T> pageResponse) {
    return ApiResponse.<PageResponse<T>>builder()
        .code(0)
        .status(status)
        .message(message)
        .data(pageResponse)
        .timestamp(System.currentTimeMillis())
        .build();
  }

  /**
   * Success với PageResponse rỗng - Dùng khi không có data
   *
   * @param message - Custom message
   * @return ApiResponse wrap empty PageResponse
   */
  public static <T> ApiResponse<PageResponse<T>> successPageEmpty(String message) {
    return ApiResponse.<PageResponse<T>>builder()
        .code(0)
        .status(HttpStatus.OK.value())
        .message(message)
        .data(PageResponse.empty())
        .timestamp(System.currentTimeMillis())
        .build();
  }

  /**
   * Success với PageResponse rỗng - Message mặc định
   *
   * @return ApiResponse wrap empty PageResponse
   */
  public static <T> ApiResponse<PageResponse<T>> successPageEmpty() {
    return ApiResponse.<PageResponse<T>>builder()
        .code(0)
        .status(HttpStatus.OK.value())
        .message("No data found")
        .data(PageResponse.empty())
        .timestamp(System.currentTimeMillis())
        .build();
  }

  // ==================== PAGE RESPONSE ERROR METHODS ====================

  /**
   * Error với PageResponse - ErrorCode
   *
   * @param errorCode - Error code
   * @return ApiResponse error với empty PageResponse
   */
  public static <T> ApiResponse<PageResponse<T>> errorPage(ErrorCode errorCode) {
    return ApiResponse.<PageResponse<T>>builder()
        .code(errorCode.getCode())
        .status(errorCode.getStatus())
        .message(errorCode.getMessage())
        .data(PageResponse.empty())
        .timestamp(System.currentTimeMillis())
        .build();
  }

  /**
   * Error với PageResponse - ErrorCode và path
   *
   * @param errorCode - Error code
   * @param path      - Request path
   * @return ApiResponse error với empty PageResponse
   */
  public static <T> ApiResponse<PageResponse<T>> errorPageWithPath(
      ErrorCode errorCode, String path) {
    return ApiResponse.<PageResponse<T>>builder()
        .code(errorCode.getCode())
        .status(errorCode.getStatus())
        .message(errorCode.getMessage())
        .data(PageResponse.empty())
        .timestamp(System.currentTimeMillis())
        .path(path)
        .build();
  }

  /**
   * Error với PageResponse - Custom message
   *
   * @param errorCode     - Error code
   * @param customMessage - Custom error message
   * @return ApiResponse error với empty PageResponse
   */
  public static <T> ApiResponse<PageResponse<T>> errorPageWithMessage(
      ErrorCode errorCode, String customMessage) {
    return ApiResponse.<PageResponse<T>>builder()
        .code(errorCode.getCode())
        .status(errorCode.getStatus())
        .message(customMessage)
        .data(PageResponse.empty())
        .timestamp(System.currentTimeMillis())
        .build();
  }

  /**
   * Error với PageResponse - Custom message và path
   *
   * @param errorCode     - Error code
   * @param customMessage - Custom error message
   * @param path          - Request path
   * @return ApiResponse error với empty PageResponse
   */
  public static <T> ApiResponse<PageResponse<T>> errorPageWithMessageAndPath(
      ErrorCode errorCode, String customMessage, String path) {
    return ApiResponse.<PageResponse<T>>builder()
        .code(errorCode.getCode())
        .status(errorCode.getStatus())
        .message(customMessage)
        .data(PageResponse.empty())
        .timestamp(System.currentTimeMillis())
        .path(path)
        .build();
  }

  /**
   * Error với PageResponse - HTTP status
   *
   * @param status  - HTTP status code
   * @param message - Error message
   * @return ApiResponse error với empty PageResponse
   */
  public static <T> ApiResponse<PageResponse<T>> errorPageWithHttpStatus(
      int status, String message) {
    return ApiResponse.<PageResponse<T>>builder()
        .code(status)
        .status(status)
        .message(message)
        .data(PageResponse.empty())
        .timestamp(System.currentTimeMillis())
        .build();
  }

  /**
   * Error với PageResponse - HTTP status và path
   *
   * @param status  - HTTP status code
   * @param message - Error message
   * @param path    - Request path
   * @return ApiResponse error với empty PageResponse
   */
  public static <T> ApiResponse<PageResponse<T>> errorPageWithHttpStatusAndPath(
      int status, String message, String path) {
    return ApiResponse.<PageResponse<T>>builder()
        .code(status)
        .status(status)
        .message(message)
        .data(PageResponse.empty())
        .timestamp(System.currentTimeMillis())
        .path(path)
        .build();

  }
}