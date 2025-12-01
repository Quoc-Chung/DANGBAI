package com.quocchung.dangbai.duandangbai.service;


import com.quocchung.dangbai.duandangbai.dtos.response.UserResponse;

public interface UserService {
  UserResponse getUserById(Long userId);

}
