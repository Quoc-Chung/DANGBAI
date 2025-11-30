package com.quocchung.dangbai.duandangbai.dtos.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {

  @NotBlank(message = "Username không được để trống")
  @Size(min = 3, max = 50, message = "Username phải từ 3-50 ký tự")
  @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Username chỉ chứa chữ cái, số và dấu gạch dưới")
  private String username;

  @NotBlank(message = "Email không được để trống")
  @Email(message = "Email không hợp lệ")
  private String email;

  @NotBlank(message = "Password không được để trống")
  @Size(min = 6, max = 100, message = "Password phải từ 6-100 ký tự")
  private String password;

  @NotBlank(message = "Display name không được để trống")
  @Size(min = 2, max = 100, message = "Display name phải từ 2-100 ký tự")
  private String displayName;

  @Pattern(regexp = "^(\\+84|0)[0-9]{9,10}$", message = "Số điện thoại không hợp lệ")
  private String phone;
}