package com.quocchung.dangbai.duandangbai.dtos.response;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
// Khi chuyển đổi từ object sang JSON , Jackson sẽ bỏ qua  tất cả các field có giá trị
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthResponse {
  private String accessToken;
  private String refreshToken;
  private String tokenType;
  private Long userId;
  private String username;
  private String email;
  private String displayName;
  private List<String> roles;
}