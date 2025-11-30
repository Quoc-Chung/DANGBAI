package com.quocchung.dangbai.duandangbai.controller;

import static com.quocchung.dangbai.duandangbai.contants.ApiConstant.AUTH;
import static com.quocchung.dangbai.duandangbai.contants.ApiConstant.LOGIN;
import static com.quocchung.dangbai.duandangbai.contants.ApiConstant.LOGOUT;
import static com.quocchung.dangbai.duandangbai.contants.ApiConstant.REFRESH_TOKEN;
import static com.quocchung.dangbai.duandangbai.contants.ApiConstant.REGISTER;

import com.quocchung.dangbai.duandangbai.dtos.request.LoginRequest;
import com.quocchung.dangbai.duandangbai.dtos.request.RefreshTokenRequest;
import com.quocchung.dangbai.duandangbai.dtos.request.RegisterRequest;
import com.quocchung.dangbai.duandangbai.dtos.response.ApiResponse;
import com.quocchung.dangbai.duandangbai.dtos.response.AuthResponse;
import com.quocchung.dangbai.duandangbai.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(AUTH)
public class AuthController {

  private final AuthService authService;

  @PostMapping(REGISTER)
  public ResponseEntity<ApiResponse<AuthResponse>> register(
      @Valid @RequestBody RegisterRequest request,
      HttpServletRequest httpRequest
  ){
    AuthResponse response  = authService.register(request);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(ApiResponse.success("Đăng ký thành công", response));
  }
  @PostMapping(LOGIN)
  public ResponseEntity<ApiResponse<AuthResponse>> login(
      @Valid @RequestBody LoginRequest request
  ) {
    AuthResponse response = authService.login(request);

    return ResponseEntity.ok(
        ApiResponse.success("Đăng nhập thành công", response)
    );
  }

  @PostMapping(REFRESH_TOKEN)
  public ResponseEntity<ApiResponse<AuthResponse>> refreshToken(
      @Valid @RequestBody RefreshTokenRequest request
  ) {
    AuthResponse response = authService.refreshToken(request.getRefreshToken());

    return ResponseEntity.ok(
        ApiResponse.success("Làm mới token thành công", response)
    );
  }

  @PostMapping(LOGOUT)
  public ResponseEntity<ApiResponse<Void>> logout(
      @RequestHeader("Authorization") String authHeader
  ) {
    String token = authHeader.substring(7);
    authService.logout(token);

    return ResponseEntity.ok(
        ApiResponse.successWithMessage("Đăng xuất thành công")
    );
  }
}
