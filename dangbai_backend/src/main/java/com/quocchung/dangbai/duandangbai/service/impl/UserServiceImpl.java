package com.quocchung.dangbai.duandangbai.service.impl;

import com.quocchung.dangbai.duandangbai.dtos.response.UserResponse;
import com.quocchung.dangbai.duandangbai.model.User;
import com.quocchung.dangbai.duandangbai.repository.UserRepository;
import com.quocchung.dangbai.duandangbai.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  @Override
  public UserResponse getUserById(Long userId) {
    return  mapToResponse(userRepository.getById(userId));
  }

  public UserResponse mapToResponse(User user) {
    return UserResponse.builder()
        .id(user.getId())
        .email(user.getEmail())
        .username(user.getUsername())
        .displayName(user.getDisplayName())
        .phone(user.getPhone())
        .bio(user.getBio())
        .avatarUrl(user.getAvatarUrl())
        .accountStatus(user.getAccountStatus().name())
        .build();
  }
}
