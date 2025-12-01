package com.quocchung.dangbai.duandangbai.controller;

import static com.quocchung.dangbai.duandangbai.contants.ApiConstant.REGISTER;
import static com.quocchung.dangbai.duandangbai.contants.ApiConstant.USER;
import static com.quocchung.dangbai.duandangbai.contants.ApiConstant.USER_PROFILE;

import com.quocchung.dangbai.duandangbai.dtos.request.RegisterRequest;
import com.quocchung.dangbai.duandangbai.dtos.response.ApiResponse;
import com.quocchung.dangbai.duandangbai.dtos.response.AuthResponse;
import com.quocchung.dangbai.duandangbai.dtos.response.UserResponse;
import com.quocchung.dangbai.duandangbai.service.JwtService;
import com.quocchung.dangbai.duandangbai.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(USER)
@RequiredArgsConstructor
public class UserController {
  private final JwtService jwtService;
  private final UserService userService;


  @GetMapping(USER_PROFILE)
  public ResponseEntity<ApiResponse<UserResponse>> getMyInfo(
      @RequestHeader("Authorization") String authHeader
  ) {
    // Lấy token từ header "Bearer xxx"
    String token = authHeader.substring(7);

    // Giải mã userId từ token
    Long userId = jwtService.extractUserId(token);

    // Lấy user từ DB
    UserResponse user = userService.getUserById(userId);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(ApiResponse.success("Lấy thông tin thành công ", user));

  }
}
