package com.quocchung.dangbai.duandangbai.service;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;

public interface JwtService {

  // Tạo Access Token
  String generateToken(UserDetails userDetails);

  String generateToken(Map<String, Object> extraClaims, UserDetails userDetails);

  // Tạo Refresh Token
  String generateRefreshToken(UserDetails userDetails);

  // Validate Token
  boolean isTokenValid(String token, UserDetails userDetails);

  // Lấy ra username từ token
  String extractUsername(String token);

  // Lấy ra userId từ token
  Long extractUserId(String token);

  // Lấy cả userInfo từ token (id,  name)
  Map<String, Object> extractUserInfo(String token);

  // Lấy ra ngày hết hạn của token
  java.util.Date extractExpiration(String token);

  // Trích xuất bất kì thông tin nào từ token
  <T> T extractClaim(String token, java.util.function.Function<io.jsonwebtoken.Claims, T> claimsResolver);
}
