package com.quocchung.dangbai.duandangbai.service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenStorageService {

  private final RedisTemplate<String, Object> redisTemplate;

  // Prefix cho các keys trong Redis
  private static final String ACCESS_TOKEN_PREFIX = "access_token:";
  private static final String REFRESH_TOKEN_PREFIX = "refresh_token:";
  private static final String USER_TOKENS_PREFIX = "user_tokens:";

  /**
   * Lưu Access Token vào Redis với TTL tự động
   * @param token Access token
   * @param userId User ID
   * @param expirationInSeconds Thời gian hết hạn (giây)
   */
  public void saveAccessToken(String token, Long userId, long expirationInSeconds) {
    String key = ACCESS_TOKEN_PREFIX + token;

    try {
      redisTemplate.opsForValue().set(key, userId, expirationInSeconds, TimeUnit.SECONDS);
      log.debug("Saved access token for user {} with TTL {} seconds", userId, expirationInSeconds);
    } catch (Exception e) {
      log.error("Error saving access token to Redis: {}", e.getMessage());
    }
  }

  /**
   * Lưu Refresh Token vào Redis với TTL tự động
   * @param token Refresh token
   * @param userId User ID
   * @param expirationInSeconds Thời gian hết hạn (giây)
   */
  public void saveRefreshToken(String token, Long userId, long expirationInSeconds) {
    String key = REFRESH_TOKEN_PREFIX + token;

    try {
      redisTemplate.opsForValue().set(key, userId, expirationInSeconds, TimeUnit.SECONDS);
      log.debug("Saved refresh token for user {} with TTL {} seconds", userId, expirationInSeconds);
    } catch (Exception e) {
      log.error("Error saving refresh token to Redis: {}", e.getMessage());
    }
  }

  /**
   * Lưu cả Access Token và Refresh Token cho user
   * Redis sẽ tự động xóa khi hết hạn (TTL)
   */
  public void saveUserTokens(Long userId, String accessToken, String refreshToken,
      long accessTokenTTL, long refreshTokenTTL) {
    // Lưu access token
    saveAccessToken(accessToken, userId, accessTokenTTL);

    // Lưu refresh token
    saveRefreshToken(refreshToken, userId, refreshTokenTTL);

    // Lưu mapping user -> tokens (để logout tất cả devices)
    String userKey = USER_TOKENS_PREFIX + userId;
    try {
      redisTemplate.opsForSet().add(userKey, accessToken);
      redisTemplate.opsForSet().add(userKey, refreshToken);
      // Set TTL cho user tokens = refresh token TTL
      redisTemplate.expire(userKey, refreshTokenTTL, TimeUnit.SECONDS);
      log.info("Saved tokens for user {}", userId);
    } catch (Exception e) {
      log.error("Error saving user tokens mapping: {}", e.getMessage());
    }
  }

  /**
   * Kiểm tra Access Token có tồn tại trong Redis không
   * @param token Access token
   * @return true nếu token hợp lệ (tồn tại trong Redis)
   */
  public boolean isAccessTokenValid(String token) {
    String key = ACCESS_TOKEN_PREFIX + token;
    try {
      return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    } catch (Exception e) {
      log.error("Error checking access token: {}", e.getMessage());
      return false;
    }
  }

  /**
   * Kiểm tra Refresh Token có tồn tại trong Redis không
   * @param token Refresh token
   * @return true nếu token hợp lệ
   */
  public boolean isRefreshTokenValid(String token) {
    String key = REFRESH_TOKEN_PREFIX + token;
    try {
      return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    } catch (Exception e) {
      log.error("Error checking refresh token: {}", e.getMessage());
      return false;
    }
  }

  /**
   * Lấy User ID từ Access Token
   * @param token Access token
   * @return User ID hoặc null nếu không tìm thấy
   */
  public Long getUserIdFromAccessToken(String token) {
    String key = ACCESS_TOKEN_PREFIX + token;
    try {
      Object userId = redisTemplate.opsForValue().get(key);
      return userId != null ? Long.valueOf(userId.toString()) : null;
    } catch (Exception e) {
      log.error("Error getting user ID from access token: {}", e.getMessage());
      return null;
    }
  }

  /**
   * Lấy User ID từ Refresh Token
   */
  public Long getUserIdFromRefreshToken(String token) {
    String key = REFRESH_TOKEN_PREFIX + token;
    try {
      Object userId = redisTemplate.opsForValue().get(key);
      return userId != null ? Long.valueOf(userId.toString()) : null;
    } catch (Exception e) {
      log.error("Error getting user ID from refresh token: {}", e.getMessage());
      return null;
    }
  }

  /**
   * Xóa Access Token khỏi Redis (Logout)
   * @param token Access token
   */
  public void deleteAccessToken(String token) {
    String key = ACCESS_TOKEN_PREFIX + token;
    try {
      redisTemplate.delete(key);
      log.debug("Deleted access token from Redis");
    } catch (Exception e) {
      log.error("Error deleting access token: {}", e.getMessage());
    }
  }

  /**
   * Xóa Refresh Token khỏi Redis
   * @param token Refresh token
   */
  public void deleteRefreshToken(String token) {
    String key = REFRESH_TOKEN_PREFIX + token;
    try {
      redisTemplate.delete(key);
      log.debug("Deleted refresh token from Redis");
    } catch (Exception e) {
      log.error("Error deleting refresh token: {}", e.getMessage());
    }
  }

  /**
   * Xóa tất cả tokens của user (Logout all devices)
   * @param userId User ID
   */
  public void deleteAllUserTokens(Long userId) {
    String userKey = USER_TOKENS_PREFIX + userId;
    try {
      // Lấy tất cả tokens của user
      var tokens = redisTemplate.opsForSet().members(userKey);

      if (tokens != null && !tokens.isEmpty()) {
        for (Object token : tokens) {
          String tokenStr = token.toString();
          // Xóa từng token
          deleteAccessToken(tokenStr);
          deleteRefreshToken(tokenStr);
        }
      }

      // Xóa user tokens mapping
      redisTemplate.delete(userKey);
      log.info("Deleted all tokens for user {}", userId);
    } catch (Exception e) {
      log.error("Error deleting all user tokens: {}", e.getMessage());
    }
  }

  /**
   * Lấy thời gian còn lại của token (TTL)
   * @param token Access token
   * @return Thời gian còn lại (giây), -1 nếu không tồn tại, -2 nếu không có TTL
   */
  public Long getTokenTTL(String token) {
    String key = ACCESS_TOKEN_PREFIX + token;
    try {
      return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    } catch (Exception e) {
      log.error("Error getting token TTL: {}", e.getMessage());
      return -1L;
    }
  }

  /**
   * Kiểm tra và log thông tin token
   */
  public void logTokenInfo(String token) {
    String key = ACCESS_TOKEN_PREFIX + token;
    Long ttl = redisTemplate.getExpire(key, TimeUnit.SECONDS);
    Long userId = getUserIdFromAccessToken(token);
    log.info("Token info - User: {}, TTL: {} seconds", userId, ttl);
  }
}