package com.quocchung.dangbai.duandangbai.service.impl;

import com.quocchung.dangbai.duandangbai.dtos.request.LoginRequest;
import com.quocchung.dangbai.duandangbai.dtos.request.RegisterRequest;
import com.quocchung.dangbai.duandangbai.dtos.response.AuthResponse;
import com.quocchung.dangbai.duandangbai.exception.AuthenticationException;
import com.quocchung.dangbai.duandangbai.exception.ErrorCode;
import com.quocchung.dangbai.duandangbai.model.Role;
import com.quocchung.dangbai.duandangbai.model.User;
import com.quocchung.dangbai.duandangbai.repository.RoleRepository;
import com.quocchung.dangbai.duandangbai.repository.UserRepository;
import com.quocchung.dangbai.duandangbai.security.CustomUserDetails;
import com.quocchung.dangbai.duandangbai.service.AuthService;
import com.quocchung.dangbai.duandangbai.service.JwtService;
import com.quocchung.dangbai.duandangbai.service.TokenStorageService;
import com.quocchung.dangbai.duandangbai.utils.enums.AccountStatus;
import com.quocchung.dangbai.duandangbai.utils.token.JwtProperties;
import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;
  private final TokenStorageService tokenStorageService;
  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;
  private final JwtProperties jwtProperties;

  @Override
  @Transactional
  public AuthResponse register(RegisterRequest request) {
    if(userRepository.existsByUsername(request.getUsername())){
      throw new AuthenticationException(ErrorCode.USER_ALREADY_EXISTS, "Username "+ request.getUsername() + " đã tồn tại");
    }
    if(userRepository.existsByEmail(request.getEmail())){
      throw new AuthenticationException(ErrorCode.EMAIL_ALREADY_EXISTS, "Email "+ request.getEmail() + " đã tồn tại");
    }

    Role userRole = roleRepository.findByName("ROLE_USER")
        .orElseThrow(() -> new RuntimeException("Role USER not found"));
    Set<Role> roles = new HashSet<>();
    roles.add(userRole);

    User user = User.builder()
        .username(request.getUsername())
        .email(request.getEmail())
        .passwordHash(passwordEncoder.encode(request.getPassword()))
        .displayName(request.getDisplayName())
        .phone(request.getPhone())
        .accountStatus(AccountStatus.ACTIVE)
        .roles(roles)
        .build();

    user = userRepository.save(user);
    CustomUserDetails userDetails = CustomUserDetails.build(user);

    String accessToken = jwtService.generateToken(userDetails);
    String refreshToken = jwtService.generateRefreshToken(userDetails);

    tokenStorageService.saveUserTokens(
        user.getId(),
        accessToken,
        refreshToken,
        jwtProperties.getExpiration()/1000,
        jwtProperties.getRefreshToken().getExpiration() /1000
    );

    return AuthResponse.builder()
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .tokenType("Bearer")
        .userId(user.getId())
        .username(user.getUsername())
        .email(user.getEmail())
        .displayName(user.getDisplayName())
        .roles(user.getRoles().stream().map(Role::getName).toList())
        .build();
  }

  @Override
  @Transactional(readOnly = true)
  public AuthResponse login(LoginRequest request) {
    try {
      Authentication authentication = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
      );

      CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

      if(userDetails.getAccountStatus() == AccountStatus.SUSPENDED){
        throw new AuthenticationException(ErrorCode.ACCOUNT_SUSPENDED, "Tài khoản bị tạm khóa");
      }
      if(userDetails.getAccountStatus() == AccountStatus.BANNED){
        throw new AuthenticationException(ErrorCode.ACCOUNT_LOCKED, "Tài khoản bị cấm");
      }

      String accessToken = jwtService.generateToken(userDetails);
      String refreshToken = jwtService.generateRefreshToken(userDetails);

      tokenStorageService.saveUserTokens(
          userDetails.getId(),
          accessToken,
          refreshToken,
          jwtProperties.getExpiration()/1000,
          jwtProperties.getRefreshToken().getExpiration() /1000
      );

      User user = userRepository.findByUsername(userDetails.getUsername())
          .orElseThrow(() -> new AuthenticationException(ErrorCode.USER_NOT_FOUND));

      return AuthResponse.builder()
          .accessToken(accessToken)
          .refreshToken(refreshToken)
          .tokenType("Bearer")
          .userId(userDetails.getId())
          .username(userDetails.getUsername())
          .email(userDetails.getEmail())
          .displayName(userDetails.getDisplayName())
          .roles(user.getRoles().stream().map(Role::getName).toList())
          .build();
    } catch (BadCredentialsException e) {
      throw new AuthenticationException(ErrorCode.INVALID_CREDENTIALS, "Username hoặc password không chính xác");
    } catch (DisabledException e) {
      throw new AuthenticationException(ErrorCode.ACCOUNT_BANNED, "Tài khoản đã bị vô hiệu hóa");
    }
  }

  @Override
  @Transactional(readOnly = true)
  public AuthResponse refreshToken(String refreshToken) {
    String username = jwtService.extractUsername(refreshToken);
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new AuthenticationException(ErrorCode.USER_NOT_FOUND));

    CustomUserDetails userDetails = CustomUserDetails.build(user);

    if(!jwtService.isTokenValid(refreshToken, userDetails)){
      throw new AuthenticationException(ErrorCode.TOKEN_INVALID, "Refresh token không hợp lệ");
    }

    String newAccessToken = jwtService.generateToken(userDetails);
    String newRefreshToken = jwtService.generateRefreshToken(userDetails);

    tokenStorageService.saveUserTokens(
        user.getId(),
        newAccessToken,
        newRefreshToken,
        jwtProperties.getExpiration()/1000,
        jwtProperties.getRefreshToken().getExpiration() /1000
    );

    return AuthResponse.builder()
        .accessToken(newAccessToken)
        .refreshToken(newRefreshToken)
        .tokenType("Bearer")
        .userId(user.getId())
        .username(user.getUsername())
        .email(user.getEmail())
        .displayName(user.getDisplayName())
        .roles(user.getRoles().stream().map(Role::getName).toList())
        .build();
  }

  @Override
  public void logout(String token){
    tokenStorageService.deleteAccessToken(token);
    log.info("User logged out, token removed");
  }
}
