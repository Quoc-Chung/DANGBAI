package com.quocchung.dangbai.duandangbai.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

  // ==================== COMMON ERRORS (1xxx) ====================
  INTERNAL_SERVER_ERROR(1000, 500, "Internal server error"),
  INVALID_REQUEST(1001, 400, "Invalid request"),
  UNAUTHORIZED(1002, 401, "Unauthorized - Please login"),
  FORBIDDEN(1003, 403, "Forbidden - You don't have permission"),
  NOT_FOUND(1004, 404, "Resource not found"),
  METHOD_NOT_ALLOWED(1005, 405, "Method not allowed"),
  REQUEST_TIMEOUT(1006, 408, "Request timeout"),
  TOO_MANY_REQUESTS(1007, 429, "Too many requests - Please try again later"),
  SERVICE_UNAVAILABLE(1008, 503, "Service temporarily unavailable"),

  // ==================== VALIDATION ERRORS (2xxx) ====================
  VALIDATION_ERROR(2000, 400, "Validation error"),
  MISSING_REQUIRED_FIELD(2001, 400, "Missing required field"),
  INVALID_FORMAT(2002, 400, "Invalid format"),
  INVALID_EMAIL_FORMAT(2003, 400, "Invalid email format"),
  INVALID_PHONE_FORMAT(2004, 400, "Invalid phone number format"),
  INVALID_PASSWORD_FORMAT(2005, 400, "Password must be at least 8 characters with uppercase, lowercase, number and special character"),
  INVALID_USERNAME_FORMAT(2006, 400, "Username must be 3-50 characters, alphanumeric and underscore only"),
  INVALID_URL_FORMAT(2007, 400, "Invalid URL format"),
  INVALID_STATUS(2015, 400, "Trạng thái không hợp lệ"),
  INVALID_DATE_FORMAT(2008, 400, "Invalid date format"),
  INVALID_ENUM_VALUE(2009, 400, "Invalid enum value"),
  VALUE_TOO_SHORT(2010, 400, "Value is too short"),
  VALUE_TOO_LONG(2011, 400, "Value is too long"),
  VALUE_OUT_OF_RANGE(2012, 400, "Value is out of allowed range"),
  INVALID_FILE_TYPE(2013, 400, "Invalid file type"),
  FILE_TOO_LARGE(2014, 400, "File size exceeds maximum limit"),

  // ==================== AUTHENTICATION ERRORS (3xxx) ====================

  INVALID_CREDENTIALS(3000, 401, "Invalid username or password"),
  ACCOUNT_LOCKED(3001, 403, "Account has been locked"),
  ACCOUNT_SUSPENDED(3002, 403, "Account has been suspended"),
  ACCOUNT_NOT_ACTIVATED(3003, 403, "Account is not activated"),
  ACCOUNT_BANNED(3013 , 430,""),
  TOKEN_EXPIRED(3004, 401, "Token has expired"),
  TOKEN_INVALID(3005, 401, "Invalid token"),
  REFRESH_TOKEN_EXPIRED(3006, 401, "Refresh token has expired"),
  REFRESH_TOKEN_INVALID(3007, 401, "Invalid refresh token"),
  PASSWORD_EXPIRED(3008, 401, "Password has expired - Please reset"),
  SESSION_EXPIRED(3009, 401, "Session has expired - Please login again"),
  INVALID_OTP(3010, 400, "Invalid OTP code"),
  OTP_EXPIRED(3011, 400, "OTP code has expired"),
  TOO_MANY_LOGIN_ATTEMPTS(3012, 429, "Too many login attempts - Account temporarily locked"),

  // ==================== AUTHORIZATION ERRORS (4xxx) ====================
  INSUFFICIENT_PERMISSIONS(4000, 403, "You don't have sufficient permissions"),
  ROLE_NOT_FOUND(4001, 404, "Role not found"),
  PERMISSION_NOT_FOUND(4002, 404, "Permission not found"),
  CANNOT_MODIFY_OWN_ROLE(4003, 403, "Cannot modify your own role"),
  CANNOT_DELETE_ADMIN_ROLE(4004, 403, "Cannot delete admin role"),
  MUST_HAVE_AT_LEAST_ONE_ADMIN(4005, 403, "System must have at least one admin"),

  // ==================== USER ERRORS (5xxx) ====================
  USER_NOT_FOUND(5000, 404, "User not found"),
  USER_ALREADY_EXISTS(5001, 409, "User already exists"),
  EMAIL_ALREADY_EXISTS(5002, 409, "Email already exists"),
  USERNAME_ALREADY_EXISTS(5003, 409, "Username already exists"),
  PHONE_ALREADY_EXISTS(5004, 409, "Phone number already exists"),
  OLD_PASSWORD_INCORRECT(5005, 400, "Old password is incorrect"),
  NEW_PASSWORD_SAME_AS_OLD(5006, 400, "New password must be different from old password"),
  CANNOT_DELETE_SELF(5007, 403, "Cannot delete your own account"),
  CANNOT_DEACTIVATE_SELF(5008, 403, "Cannot deactivate your own account"),
  USER_HAS_ACTIVE_POSTS(5009, 409, "Cannot delete user with active posts"),

  // ==================== POST ERRORS (6xxx) ====================
  POST_NOT_FOUND(6000, 404, "Post not found"),
  POST_ALREADY_APPROVED(6001, 409, "Post is already approved"),
  POST_ALREADY_REJECTED(6002, 409, "Post is already rejected"),
  POST_NOT_PENDING(6003, 400, "Only pending posts can be approved or rejected"),
  CANNOT_EDIT_APPROVED_POST(6004, 403, "Cannot edit approved post"),
  CANNOT_DELETE_APPROVED_POST(6005, 403, "Cannot delete approved post - Please contact admin"),
  NOT_POST_OWNER(6006, 403, "You are not the owner of this post"),
  POST_TITLE_REQUIRED(6007, 400, "Post title is required"),
  POST_DESCRIPTION_REQUIRED(6008, 400, "Post description is required"),
  POST_PRICE_INVALID(6009, 400, "Post price must be greater than 0"),
  POST_CATEGORY_REQUIRED(6010, 400, "Post category is required"),
  POST_LOCATION_REQUIRED(6011, 400, "Post location is required"),
  REJECTION_REASON_REQUIRED(6012, 400, "Rejection reason is required when rejecting post"),
  POST_MEDIA_REQUIRED(6013, 400, "At least one media file is required"),
  TOO_MANY_MEDIA_FILES(6014, 400, "Maximum 10 media files allowed per post"),
  POST_EXPIRED(6015, 410, "Post has expired"),

  // ==================== COMMENT ERRORS (7xxx) ====================
  COMMENT_NOT_FOUND(7000, 404, "Comment not found"),
  COMMENT_CONTENT_REQUIRED(7001, 400, "Comment content is required"),
  COMMENT_TOO_LONG(7002, 400, "Comment exceeds maximum length"),
  CANNOT_REPLY_TO_REPLY(7003, 400, "Cannot reply to a reply - Maximum 2 levels"),
  NOT_COMMENT_OWNER(7004, 403, "You are not the owner of this comment"),
  PARENT_COMMENT_NOT_FOUND(7005, 404, "Parent comment not found"),
  COMMENT_ON_DIFFERENT_POST(7006, 400, "Parent comment belongs to different post"),
  CANNOT_DELETE_COMMENT_WITH_REPLIES(7007, 409, "Cannot delete comment that has replies"),

  // ==================== CATEGORY ERRORS (8xxx) ====================
  CATEGORY_NOT_FOUND(8000, 404, "Category not found"),
  CATEGORY_ALREADY_EXISTS(8001, 409, "Category already exists"),
  CATEGORY_NAME_REQUIRED(8002, 400, "Category name is required"),
  CANNOT_DELETE_CATEGORY_WITH_POSTS(8003, 409, "Cannot delete category that has posts"),

  // ==================== REACTION ERRORS (9xxx) ====================
  REACTION_NOT_FOUND(9000, 404, "Reaction not found"),
  REACTION_ALREADY_EXISTS(9001, 409, "You already reacted to this"),
  INVALID_REACTION_TYPE(9002, 400, "Invalid reaction type"),
  CANNOT_REACT_TO_OWN_POST(9003, 403, "Cannot react to your own post"),
  CANNOT_REACT_TO_OWN_COMMENT(9004, 403, "Cannot react to your own comment"),
  REACTION_TARGET_REQUIRED(9005, 400, "Reaction must target either a post or comment"),
  REACTION_MULTIPLE_TARGETS(9006, 400, "Reaction cannot target both post and comment"),

  // ==================== NOTIFICATION ERRORS (10xxx) ====================
  NOTIFICATION_NOT_FOUND(10000, 404, "Notification not found"),
  NOT_NOTIFICATION_OWNER(10001, 403, "You are not the owner of this notification"),
  NOTIFICATION_ALREADY_READ(10002, 409, "Notification is already marked as read"),

  // ==================== CONTACT ERRORS (11xxx) ====================
  CONTACT_NOT_FOUND(11000, 404, "Contact request not found"),
  CANNOT_CONTACT_SELF(11001, 403, "Cannot contact yourself"),
  CONTACT_MESSAGE_REQUIRED(11002, 400, "Contact message is required"),
  CONTACT_ALREADY_EXISTS(11003, 409, "You already contacted about this post"),
  POST_NOT_AVAILABLE_FOR_CONTACT(11004, 403, "This post is not available for contact"),

  // ==================== MEDIA/FILE ERRORS (12xxx) ====================
  FILE_UPLOAD_FAILED(12000, 500, "File upload failed"),
  FILE_NOT_FOUND(12001, 404, "File not found"),
  INVALID_IMAGE_FORMAT(12002, 400, "Invalid image format - Only JPG, PNG, GIF allowed"),
  INVALID_VIDEO_FORMAT(12003, 400, "Invalid video format - Only MP4, AVI, MOV allowed"),
  IMAGE_TOO_LARGE(12004, 400, "Image size exceeds 5MB limit"),
  VIDEO_TOO_LARGE(12005, 400, "Video size exceeds 100MB limit"),
  MEDIA_PROCESSING_FAILED(12006, 500, "Media processing failed"),
  THUMBNAIL_GENERATION_FAILED(12007, 500, "Thumbnail generation failed"),

  // ==================== DATABASE ERRORS (13xxx) ====================
  DATABASE_ERROR(13000, 500, "Database error occurred"),
  CONSTRAINT_VIOLATION(13001, 409, "Database constraint violation"),
  DUPLICATE_KEY_ERROR(13002, 409, "Duplicate key error"),
  FOREIGN_KEY_VIOLATION(13003, 409, "Referenced record does not exist"),
  CONNECTION_ERROR(13004, 503, "Database connection error"),
  TRANSACTION_FAILED(13005, 500, "Database transaction failed"),
  DEADLOCK_DETECTED(13006, 409, "Database deadlock detected - Please try again"),

  // ==================== EXTERNAL SERVICE ERRORS (14xxx) ====================
  EMAIL_SERVICE_ERROR(14000, 500, "Email service error"),
  SMS_SERVICE_ERROR(14001, 500, "SMS service error"),
  STORAGE_SERVICE_ERROR(14002, 500, "Storage service error"),
  PAYMENT_SERVICE_ERROR(14003, 500, "Payment service error"),
  NOTIFICATION_SERVICE_ERROR(14004, 500, "Notification service error"),

  // ==================== BUSINESS LOGIC ERRORS (15xxx) ====================
  OPERATION_NOT_ALLOWED(15000, 403, "Operation not allowed in current state"),
  CONCURRENT_MODIFICATION(15001, 409, "Resource was modified by another user"),
  RESOURCE_LOCKED(15002, 423, "Resource is locked by another process"),
  QUOTA_EXCEEDED(15003, 429, "You have exceeded your quota"),
  DAILY_POST_LIMIT_EXCEEDED(15004, 429, "Daily post limit exceeded"),
  SPAM_DETECTED(15005, 429, "Spam detected - Action blocked");

  private final int code;      // Mã lỗi nội bộ
  private final int status;    // HTTP status code
  private final String message; // Thông báo lỗi

}