package com.quocchung.dangbai.duandangbai.service;

import com.quocchung.dangbai.duandangbai.dtos.request.LoginRequest;
import com.quocchung.dangbai.duandangbai.dtos.request.RegisterRequest;
import com.quocchung.dangbai.duandangbai.dtos.response.AuthResponse;

public interface AuthService {

  AuthResponse register(RegisterRequest request);

  AuthResponse login(LoginRequest request);

  AuthResponse refreshToken(String refreshToken);

  void  logout(String token);

}
