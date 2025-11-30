package com.quocchung.dangbai.duandangbai.service.impl;
import com.quocchung.dangbai.duandangbai.security.CustomUserDetails;
import com.quocchung.dangbai.duandangbai.service.JwtService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class JwtServiceImpl implements JwtService {

  @Value("${jwt.secret}")
  private String secretKey;

  @Value("${jwt.expiration}")
  private long jwtExpiration;

  @Value("${jwt.refresh-token.expiration}")
  private long refreshExpiration;

  /**
   * Tạo ra token từ thông tin người dùng không cs extract claims
   * extract claims: subject- username hoặc định đanh chính của user
   *                 iat- thời điểm token đc tạo tính bằng giây /milisecond
   *                 exp - thời điểm token hết hạn
   *                 authorities : danh sách quyền
   *                 email, userid , avatar (duong dan)
   * @param userDetails
   * @return
   */
  @Override
  public String generateToken(UserDetails userDetails) {
    return generateToken(new HashMap<>(), userDetails);
  }

  @Override
  public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
    if (userDetails instanceof CustomUserDetails customUser) {
      extraClaims.put("userId", customUser.getId());   // Thêm userId
    }
    extraClaims.put("authorities", userDetails.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.toList()));
    return buildToken(extraClaims, userDetails, jwtExpiration);
  }

  @Override
  public String generateRefreshToken(UserDetails userDetails) {
    return buildToken(new HashMap<>(), userDetails, refreshExpiration);
  }

  /**
   * Hàm build token
   * @param extraClaims dữ liệu bổ xung bạn theem vào token
   * @param userDetails thông tin người dùng
   * @param expiration  thông tin hết hạn
   * @return chuỗi token
   */
  private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, long expiration) {
    return Jwts.builder()
        .setClaims(extraClaims)
        .setSubject(userDetails.getUsername())

        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + expiration))
        .signWith(getSignInKey())
        .compact();
  }

  /**
   * Kiểm tra token hợp lệ hay không
   * @param token
   * @param userDetails
   * @return
   */
  @Override
  public boolean isTokenValid(String token, UserDetails userDetails) {
    // lấy ra tên của người dùng
    final String username = extractUsername(token);
    // kiểm tra tên của người dùng và check xem còn hạn không
    return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
  }

  @Override
  public String extractUsername(String token) {

    return extractClaim(token, Claims::getSubject);
  }

  @Override
  public Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  /**
   * Hàm đọc generic claim trong token
   * @param token
   * @param claimsResolver
   * @return
   * @param <T>
   */
  @Override
  public <T> T extractClaim(String token, java.util.function.Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  /**
   * Hàm lấy ra tất cả thông tin trong token
   * @param token
   * @return
   */
  private Claims extractAllClaims(String token) {
    try {
      return Jwts.parser()
          .verifyWith(getSignInKey())
          .build()
          .parseSignedClaims(token)
          .getPayload();
    } catch (ExpiredJwtException e) {
      log.error("JWT token is expired: {}", e.getMessage());
      throw e;
    } catch (UnsupportedJwtException e) {
      log.error("JWT token is unsupported: {}", e.getMessage());
      throw e;
    } catch (MalformedJwtException e) {
      log.error("Invalid JWT token: {}", e.getMessage());
      throw e;
    } catch (SignatureException e) {
      log.error("Invalid JWT signature: {}", e.getMessage());
      throw e;
    } catch (IllegalArgumentException e) {
      log.error("JWT claims string is empty: {}", e.getMessage());
      throw e;
    }
  }

  private boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  private SecretKey getSignInKey() {
    byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  /**
   * get id from token
   * @param token
   * @return id
   */
  @Override
  public Long extractUserId(String token) {
    return extractClaim(token, claims -> claims.get("userId", Long.class));
  }

  /**
   * Get all information user from token
   * @param token
   * @return
   */
  @Override
  public Map<String, Object> extractUserInfo(String token) {
    return extractClaim(token, claims -> {
      Map<String, Object> map = new HashMap<>();
      map.put("userId", claims.get("userId", Long.class));
      map.put("username", claims.getSubject());
      return map;
    });
  }
}
