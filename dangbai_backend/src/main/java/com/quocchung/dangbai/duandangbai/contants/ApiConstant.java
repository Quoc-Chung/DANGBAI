package com.quocchung.dangbai.duandangbai.contants;

public class ApiConstant {

  /* ========================= AUTH ========================= */
  public static final String AUTH =  "/api/v1/auth";
  public static final String POST = "/api/v1/posts";
  public static final String NOTIFICATION = "/api/v1/notification";
  public static final String USER = "/api/v1/user";
  public static final String CATEGORY = "/api/v1/category";

  public static final String REGISTER =  "/register";          // POST
  public static final String LOGIN = "/login";                // POST
  public static final String LOGOUT =  "/logout";
  // POST

  public static final String FORGOT_PASSWORD =  "/forgot-password";  // POST
  public static final String RESET_PASSWORD = "/reset-password";    // POST (sau khi nhận OTP/token)
  public static final String CHANGE_PASSWORD = "/change-password";  // PUT

  public static final String REFRESH_TOKEN = "/refresh-token";      // POST
  public static final String VERIFY_EMAIL =  "/verify-email";        // GET/POST

  /* ========================= OPTIONAL (nếu dùng OTP) ========================= */
  public static final String SEND_OTP = "/send-otp";          // POST
  public static final String VERIFY_OTP ="/verify-otp";

  /* ======================= POST ==============================*/
  public static final String CREATE_POST ="/create";
  public static final String APPROVE_POST = "/approve_post";
  public static final String FILTER_POST = "/filter";
  public static final String DETAIL_POST = "/{postId}";

  /* ======================= NOTIFYCATION =======================*/
  public static final String NOTIFICATION_BY_USER = "/user/{userId}";

  /* ======================== USER ==============================*/
  public static final String USER_PROFILE = "/profile";

  /*=========================CATEGORY ============================*/
  public static final String CATEGORY_ALL = "/all";

}
