package com.quocchung.dangbai.duandangbai.utils.token;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jwt")
@Data
public class JwtProperties {

  private String secret;
  private long expiration;
  private RefreshToken refreshToken;

  @Data
  public static class RefreshToken {
    private long expiration;
  }
}
