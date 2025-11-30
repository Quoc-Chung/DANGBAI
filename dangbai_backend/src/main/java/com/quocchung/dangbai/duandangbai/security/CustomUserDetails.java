package com.quocchung.dangbai.duandangbai.security;

import com.quocchung.dangbai.duandangbai.model.User;
import com.quocchung.dangbai.duandangbai.utils.enums.AccountStatus;
import java.util.Collection;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@AllArgsConstructor
public class CustomUserDetails  implements UserDetails {

  /*- Các thuộc tính chung của user -*/
  private Long id;
  private String username;
  private String email;
  private String password;
  private String displayName;
  private AccountStatus accountStatus;

  private Collection<? extends GrantedAuthority> authorities;
  public static CustomUserDetails build(User user) {
    // Lấy tất cả authorities từ roles và permissions
    Collection<GrantedAuthority> authorities = user.getRoles().stream()
        .flatMap(role -> {
          var roleAuthorities = role.getPermissions().stream()
              .map(permission -> new SimpleGrantedAuthority(permission.getName()))
              .collect(Collectors.toList());

          // Thêm ROLE_ prefix cho role
          roleAuthorities.add(new SimpleGrantedAuthority(role.getName()));

          return roleAuthorities.stream();
        })
        .collect(Collectors.toSet());

    return new CustomUserDetails(
        user.getId(),
        user.getUsername(),
        user.getEmail(),
        user.getPasswordHash(),
        user.getDisplayName(),
        user.getAccountStatus(),
        authorities
    );
  }

  /*- Trả về danh sách các quyền  -*/
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  /*- Trả về password -*/
  @Override
  public @Nullable String getPassword() {
    return password;
  }

  /*- Trả về username -*/
  @Override
  public String getUsername() {
    return username;
  }

  /**
   * Kiểm tra tài khoản đã hết hạn chưa
   * Trả ve true : Tài khoản chưa hết hạn (vẫn hợp lệ)
   * Trả về false: Tài khoản hết hạn , không thể login
   * @return
   */
  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  /**
   * Kiểm tra tài khoản có bị khóa không
   * True: Tài khoản không bị khóa, login bình thường
   * False: Tài khoản bị khóa, không thể login
   * @return
   */
  @Override
  public boolean isAccountNonLocked() {
    return accountStatus != AccountStatus.BANNED &&
           accountStatus!= AccountStatus.SUSPENDED;
  }

  /**
   * Kiểm tra mật khẩu có hết hạn hay không
   * True : Mật khẩu còn hiệu lực
   * False: Mật khẩu hết hạn, người dùng cần đổi pass
   * @return
   */
  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  /**
   * Kiểm tra tài khoản có được kích hoạt không
   * True: Tài khoản đang kích hoạt -> cho phép login
   * False: Tài khoản bị vô hiệu hóa (disable)
   * @return
   */
  @Override
  public boolean isEnabled() {
    return accountStatus == AccountStatus.ACTIVE;
  }
}
