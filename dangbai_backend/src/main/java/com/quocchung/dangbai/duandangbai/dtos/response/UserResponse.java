package com.quocchung.dangbai.duandangbai.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
  private Long id;
  private String email;
  private String username;
  private String displayName;
  private String phone;
  private String bio;
  private String avatarUrl;
  private String accountStatus;
}