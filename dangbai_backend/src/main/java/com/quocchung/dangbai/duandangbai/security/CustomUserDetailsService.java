package com.quocchung.dangbai.duandangbai.security;

import com.quocchung.dangbai.duandangbai.exception.ErrorCode;
import com.quocchung.dangbai.duandangbai.model.User;
import com.quocchung.dangbai.duandangbai.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  @Transactional(readOnly = true)
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUsername(username)
        .orElseGet(() -> userRepository.findByEmail(username)
            .orElseThrow(() -> new UsernameNotFoundException(
                ErrorCode.USER_NOT_FOUND.getMessage() + ": " + username
            ))
        );

    return CustomUserDetails.build(user);
  }

  @Transactional(readOnly = true)
  public UserDetails loadUserById(Long id) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new UsernameNotFoundException(
            ErrorCode.USER_NOT_FOUND.getMessage() + " with id: " + id
        ));
    return CustomUserDetails.build(user);
  }
}
