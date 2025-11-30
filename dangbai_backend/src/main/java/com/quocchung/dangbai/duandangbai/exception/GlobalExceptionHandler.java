package com.quocchung.dangbai.duandangbai.exception;

import com.quocchung.dangbai.duandangbai.dtos.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.nio.file.AccessDeniedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  // ==================== CUSTOM EXCEPTIONS ====================

  /**
   * Xử lý BusinessException - Lỗi logic nghiệp vụ
   */
  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<ApiResponse<Object>> handleBusinessException(
      BusinessException ex,
      HttpServletRequest request) {

    log.error("Business exception: {} | Path: {}", ex.getFinalMessage(), request.getRequestURI());

    ApiResponse<Object> response = ApiResponse.errorWithPath(
        ex.getErrorCode(),
        request.getRequestURI()
    );

    // Nếu có custom message thì override
    if (ex.getCustomMessage() != null) {
      response.setMessage(ex.getCustomMessage());
    }

    return ResponseEntity
        .status(ex.getErrorCode().getStatus())
        .body(response);
  }

  /**
   * Xử lý ResourceNotFoundException - Không tìm thấy resource
   */
  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ApiResponse<Object>> handleResourceNotFoundException(
      ResourceNotFoundException ex,
      HttpServletRequest request) {

    log.error("Resource not found: {} | Path: {}", ex.getFinalMessage(), request.getRequestURI());

    ApiResponse<Object> response = ApiResponse.errorWithMessageAndPath(
        ex.getErrorCode(),
        ex.getFinalMessage(),
        request.getRequestURI()
    );

    return ResponseEntity
        .status(ex.getErrorCode().getStatus())
        .body(response);
  }

  /**
   * Xử lý ValidationException - Lỗi validation với nhiều field
   */
  @ExceptionHandler(ValidationException.class)
  public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationException(
      ValidationException ex,
      HttpServletRequest request) {

    log.error("Validation exception: {} | Errors: {} | Path: {}",
        ex.getMessage(), ex.getErrors(), request.getRequestURI());

    ApiResponse<Map<String, String>> response = ApiResponse.errorWithMessageAndData(
        ex.getErrorCode(),
        ex.getFinalMessage(),
        ex.getErrors(),
        request.getRequestURI()
    );

    return ResponseEntity
        .status(ex.getErrorCode().getStatus())
        .body(response);
  }

  /**
   * Xử lý AuthenticationException - Lỗi xác thực
   */
  @ExceptionHandler(AuthenticationException.class)
  public ResponseEntity<ApiResponse<Object>> handleAuthenticationException(
      AuthenticationException ex,
      HttpServletRequest request) {

    log.error("Authentication exception: {} | Path: {}", ex.getFinalMessage(), request.getRequestURI());

    ApiResponse<Object> response = ApiResponse.errorWithMessageAndPath(
        ex.getErrorCode(),
        ex.getFinalMessage(),
        request.getRequestURI()
    );

    return ResponseEntity
        .status(ex.getErrorCode().getStatus())
        .body(response);
  }

  /**
   * Xử lý AuthorizationException - Lỗi phân quyền
   */
  @ExceptionHandler(AuthorizationException.class)
  public ResponseEntity<ApiResponse<Object>> handleAuthorizationException(
      AuthorizationException ex,
      HttpServletRequest request) {

    log.error("Authorization exception: {} | Path: {}", ex.getFinalMessage(), request.getRequestURI());

    ApiResponse<Object> response = ApiResponse.errorWithMessageAndPath(
        ex.getErrorCode(),
        ex.getFinalMessage(),
        request.getRequestURI()
    );

    return ResponseEntity
        .status(ex.getErrorCode().getStatus())
        .body(response);
  }

  /**
   * Xử lý DatabaseException - Lỗi database
   */
  @ExceptionHandler(DatabaseException.class)
  public ResponseEntity<ApiResponse<Object>> handleDatabaseException(
      DatabaseException ex,
      HttpServletRequest request) {

    log.error("Database exception: {} | Path: {}", ex.getFinalMessage(), request.getRequestURI(), ex);

    ApiResponse<Object> response = ApiResponse.errorWithMessageAndPath(
        ex.getErrorCode(),
        ex.getFinalMessage(),
        request.getRequestURI()
    );

    return ResponseEntity
        .status(ex.getErrorCode().getStatus())
        .body(response);
  }

  /**
   * Xử lý FileUploadException - Lỗi upload file
   */
  @ExceptionHandler(FileUploadException.class)
  public ResponseEntity<ApiResponse<Object>> handleFileUploadException(
      FileUploadException ex,
      HttpServletRequest request) {

    log.error("File upload exception: {} | Path: {}", ex.getFinalMessage(), request.getRequestURI());

    ApiResponse<Object> response = ApiResponse.errorWithMessageAndPath(
        ex.getErrorCode(),
        ex.getFinalMessage(),
        request.getRequestURI()
    );

    return ResponseEntity
        .status(ex.getErrorCode().getStatus())
        .body(response);
  }

  /**
   * Xử lý ExternalServiceException - Lỗi service bên ngoài
   */
  @ExceptionHandler(ExternalServiceException.class)
  public ResponseEntity<ApiResponse<Object>> handleExternalServiceException(
      ExternalServiceException ex,
      HttpServletRequest request) {

    log.error("External service exception: {} | Path: {}", ex.getFinalMessage(), request.getRequestURI(), ex);

    ApiResponse<Object> response = ApiResponse.errorWithMessageAndPath(
        ex.getErrorCode(),
        ex.getFinalMessage(),
        request.getRequestURI()
    );

    return ResponseEntity
        .status(ex.getErrorCode().getStatus())
        .body(response);
  }

  /**
   * Xử lý RateLimitException - Lỗi rate limitRate limiting
   * là cơ chế bảo vệ server khỏi bị spam, tấn công DDoS hoặc dùng quá nhiều tài nguyên.
   */
  @ExceptionHandler(RateLimitException.class)
  public ResponseEntity<ApiResponse<Object>> handleRateLimitException(
      RateLimitException ex,
      HttpServletRequest request) {

    log.warn("Rate limit exceeded: {} | Path: {} | Retry after: {}s",
        ex.getFinalMessage(), request.getRequestURI(), ex.getRetryAfterSeconds());

    ApiResponse<Object> response = ApiResponse.errorWithMessageAndPath(
        ex.getErrorCode(),
        ex.getFinalMessage() + " - Please retry after " + ex.getRetryAfterSeconds() + " seconds",
        request.getRequestURI()
    );

    return ResponseEntity
        .status(ex.getErrorCode().getStatus())
        .header("Retry-After", String.valueOf(ex.getRetryAfterSeconds()))
        .body(response);
  }

  /**
   * Xử lý ConflictException - Lỗi conflict/duplicate
   */
  @ExceptionHandler(ConflictException.class)
  public ResponseEntity<ApiResponse<Object>> handleConflictException(
      ConflictException ex,
      HttpServletRequest request) {

    log.error("Conflict exception: {} | Path: {}", ex.getFinalMessage(), request.getRequestURI());

    ApiResponse<Object> response = ApiResponse.errorWithMessageAndPath(
        ex.getErrorCode(),
        ex.getFinalMessage(),
        request.getRequestURI()
    );

    return ResponseEntity
        .status(ex.getErrorCode().getStatus())
        .body(response);
  }

  // ==================== SPRING VALIDATION EXCEPTIONS ====================

  /**
   * Xử lý @Valid validation errors (DTO validation)
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiResponse<Map<String, String>>> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      HttpServletRequest request) {

    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach(error -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    });

    log.error("Validation failed: {} | Path: {}", errors, request.getRequestURI());

    ApiResponse<Map<String, String>> response = ApiResponse.errorWithMessageAndData(
        ErrorCode.VALIDATION_ERROR,
        "Validation failed - Please check your input",
        errors,
        request.getRequestURI()
    );

    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(response);
  }

  /**
   * Xử lý @Validated constraint violations (method parameter validation)
   */
  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ApiResponse<Map<String, String>>> handleConstraintViolation(
      ConstraintViolationException ex,
      HttpServletRequest request) {

    Map<String, String> errors = new HashMap<>();
    for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
      String propertyPath = violation.getPropertyPath().toString();
      String message = violation.getMessage();
      // Lấy tên field cuối cùng từ path
      String fieldName = propertyPath.substring(propertyPath.lastIndexOf('.') + 1);
      errors.put(fieldName, message);
    }

    log.error("Constraint violation: {} | Path: {}", errors, request.getRequestURI());

    ApiResponse<Map<String, String>> response = ApiResponse.errorWithMessageAndData(
        ErrorCode.VALIDATION_ERROR,
        "Validation failed",
        errors,
        request.getRequestURI()
    );

    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(response);
  }

  // ==================== HTTP/REQUEST EXCEPTIONS ====================

  /**
   * Xử lý malformed JSON request
   */
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ApiResponse<Object>> handleHttpMessageNotReadable(
      HttpMessageNotReadableException ex,
      HttpServletRequest request) {

    log.error("Malformed JSON request: {} | Path: {}", ex.getMessage(), request.getRequestURI());

    String message = "Malformed JSON request";
    if (ex.getMessage() != null) {
      if (ex.getMessage().contains("JSON parse error")) {
        message = "Invalid JSON format - Please check your request body";
      } else if (ex.getMessage().contains("Required request body is missing")) {
        message = "Request body is required";
      }
    }

    ApiResponse<Object> response = ApiResponse.errorWithHttpStatusAndPath(
        HttpStatus.BAD_REQUEST.value(),
        message,
        request.getRequestURI()
    );

    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(response);
  }

  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public ResponseEntity<ApiResponse<Object>> handleMethodNotSupported(
      HttpRequestMethodNotSupportedException ex,
      HttpServletRequest request) {

    log.error("Method not supported: {} | Path: {}", ex.getMethod(), request.getRequestURI());

    String supportedMethods = ex.getSupportedHttpMethods() != null
        ? ex.getSupportedHttpMethods().stream()
        .map(m -> m.name())
        .collect(Collectors.joining(", "))
        : "Unknown";


    ApiResponse<Object> response = ApiResponse.errorWithHttpStatusAndPath(
        HttpStatus.METHOD_NOT_ALLOWED.value(),
        "Method " + ex.getMethod() + " is not supported. Supported methods: " + supportedMethods,
        request.getRequestURI()
    );

    return ResponseEntity
        .status(HttpStatus.METHOD_NOT_ALLOWED)
        .body(response);
  }

  /**
   * Xử lý missing request parameter
   */
  @ExceptionHandler(MissingServletRequestParameterException.class)
  public ResponseEntity<ApiResponse<Object>> handleMissingParameter(
      MissingServletRequestParameterException ex,
      HttpServletRequest request) {

    log.error("Missing parameter: {} | Path: {}", ex.getParameterName(), request.getRequestURI());

    ApiResponse<Object> response = ApiResponse.errorWithHttpStatusAndPath(
        HttpStatus.BAD_REQUEST.value(),
        "Required parameter '" + ex.getParameterName() + "' is missing",
        request.getRequestURI()
    );

    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(response);
  }

  /**
   * Xử lý type mismatch (sai kiểu dữ liệu parameter)
   */
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ApiResponse<Object>> handleTypeMismatch(
      MethodArgumentTypeMismatchException ex,
      HttpServletRequest request) {

    log.error("Type mismatch: {} | Path: {}", ex.getName(), request.getRequestURI());

    String requiredType = ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "Unknown";

    ApiResponse<Object> response = ApiResponse.errorWithHttpStatusAndPath(
        HttpStatus.BAD_REQUEST.value(),
        "Parameter '" + ex.getName() + "' should be of type " + requiredType,
        request.getRequestURI()
    );

    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(response);
  }

  /**
   * Xử lý 404 - No handler found
   */
  @ExceptionHandler(NoHandlerFoundException.class)
  public ResponseEntity<ApiResponse<Object>> handleNoHandlerFound(
      NoHandlerFoundException ex,
      HttpServletRequest request) {

    log.error("No handler found: {} {} | Path: {}",
        ex.getHttpMethod(), ex.getRequestURL(), request.getRequestURI());

    ApiResponse<Object> response = ApiResponse.errorWithHttpStatusAndPath(
        HttpStatus.NOT_FOUND.value(),
        "Endpoint not found: " + ex.getHttpMethod() + " " + ex.getRequestURL(),
        request.getRequestURI()
    );

    return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(response);
  }

  /**
   * Xử lý file upload size exceeded
   */
  @ExceptionHandler(MaxUploadSizeExceededException.class)
  public ResponseEntity<ApiResponse<Object>> handleMaxUploadSizeExceeded(
      MaxUploadSizeExceededException ex,
      HttpServletRequest request) {

    log.error("File upload size exceeded | Path: {}", request.getRequestURI());

    ApiResponse<Object> response = ApiResponse.errorWithPath(
        ErrorCode.FILE_TOO_LARGE,
        request.getRequestURI()
    );

    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(response);
  }

  // ==================== SECURITY EXCEPTIONS ====================

  /**
   * Xử lý Access Denied (Spring Security)
   */
  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ApiResponse<Object>> handleAccessDenied(
      AccessDeniedException ex,
      HttpServletRequest request) {

    log.error("Access denied: {} | Path: {}", ex.getMessage(), request.getRequestURI());

    ApiResponse<Object> response = ApiResponse.errorWithPath(
        ErrorCode.FORBIDDEN,
        request.getRequestURI()
    );

    return ResponseEntity
        .status(HttpStatus.FORBIDDEN)
        .body(response);
  }

  // ==================== DATABASE EXCEPTIONS ====================

  /**
   * Xử lý Data Integrity Violation (Duplicate key, Foreign key, etc.)
   */
  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<ApiResponse<Object>> handleDataIntegrityViolation(
      DataIntegrityViolationException ex,
      HttpServletRequest request) {

    log.error("Data integrity violation: {} | Path: {}", ex.getMessage(), request.getRequestURI());

    String message = "Database constraint violation";
    ErrorCode errorCode = ErrorCode.CONSTRAINT_VIOLATION;

    // Phân tích chi tiết lỗi
    String rootMessage = ex.getRootCause() != null
        ? ex.getRootCause().getMessage()
        : ex.getMessage();

    if (rootMessage != null) {
      if (rootMessage.contains("Duplicate entry")) {
        message = extractDuplicateKeyMessage(rootMessage);
        errorCode = ErrorCode.DUPLICATE_KEY_ERROR;
      } else if (rootMessage.contains("foreign key constraint")) {
        message = "Cannot delete or update - Record is being referenced";
        errorCode = ErrorCode.FOREIGN_KEY_VIOLATION;
      } else if (rootMessage.contains("cannot be null")) {
        message = "Required field cannot be null";
        errorCode = ErrorCode.MISSING_REQUIRED_FIELD;
      }
    }

    ApiResponse<Object> response = ApiResponse.errorWithMessageAndPath(
        errorCode,
        message,
        request.getRequestURI()
    );

    return ResponseEntity
        .status(HttpStatus.CONFLICT)
        .body(response);
  }

  /**
   * Xử lý SQL Exception
   */
  @ExceptionHandler(SQLException.class)
  public ResponseEntity<ApiResponse<Object>> handleSQLException(
      SQLException ex,
      HttpServletRequest request) {

    log.error("SQL exception: {} | Path: {}", ex.getMessage(), request.getRequestURI(), ex);

    ApiResponse<Object> response = ApiResponse.errorWithPath(
        ErrorCode.DATABASE_ERROR,
        request.getRequestURI()
    );

    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(response);
  }

  // ==================== GENERIC EXCEPTION ====================

  /**
   * Xử lý tất cả các exception chưa được handle
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiResponse<Object>> handleGenericException(
      Exception ex,
      HttpServletRequest request) {

    log.error("Unexpected error occurred: {} | Path: {}",
        ex.getMessage(), request.getRequestURI(), ex);

    // Trong môi trường development, có thể trả thêm stack trace
    ApiResponse<Object> response = ApiResponse.errorWithPath(
        ErrorCode.INTERNAL_SERVER_ERROR,
        request.getRequestURI()
    );

    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(response);
  }

  // ==================== HELPER METHODS ====================

  /**
   * Extract duplicate key message từ SQL error
   */
  private String extractDuplicateKeyMessage(String errorMessage) {
    try {
      // Format: Duplicate entry 'value' for key 'index_name'
      if (errorMessage.contains("Duplicate entry")) {
        String value = errorMessage.substring(
            errorMessage.indexOf("'") + 1,
            errorMessage.indexOf("'", errorMessage.indexOf("'") + 1)
        );

        String key = errorMessage.substring(
            errorMessage.lastIndexOf("'") + 1,
            errorMessage.length() - 1
        );

        // Parse key name để biết field nào bị duplicate
        if (key.contains("email")) {
          return "Email '" + value + "' already exists";
        } else if (key.contains("username")) {
          return "Username '" + value + "' already exists";
        } else if (key.contains("phone")) {
          return "Phone number '" + value + "' already exists";
        }

        return "Duplicate value '" + value + "' for field '" + key + "'";
      }
    } catch (Exception e) {
      log.warn("Failed to parse duplicate key message: {}", errorMessage);
    }

    return "Duplicate entry detected";
  }
}