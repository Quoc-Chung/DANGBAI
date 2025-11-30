package com.quocchung.dangbai.duandangbai.dtos.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UpdateUserRequest {
  private String displayName;
  private String phone;
  private String bio;
  private String username;
  private MultipartFile avatar;
}