package com.quocchung.dangbai.duandangbai.model;


import com.quocchung.dangbai.duandangbai.utils.enums.AccountStatus;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
/*- Lưu trữ thông tin người dùng (cả người mua và người bán) -*/
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true)
  private String email;

  private String passwordHash;

  private String username;

  private String displayName;

  private String phone;
  /*- Avatar ảnh -*/
  private String avatarUrl;

  private String bio;

  @Enumerated(EnumType.STRING)
  private AccountStatus accountStatus = AccountStatus.ACTIVE;


  @CreatedDate
  private LocalDateTime createdAt;

  @LastModifiedDate
  private LocalDateTime updatedAt;

  @ManyToMany (fetch = FetchType.EAGER)
  @JoinTable(
      name = "user_roles",
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name="role_id")
  )
  private Set<Role> roles = new HashSet<>();

 /*- 1 user có thể đăng nhiều bài viết -*/
  @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Post> posts = new ArrayList<>();

  /*- 1 user có thể có nhiều comments -*/
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Comment> comments = new ArrayList<>();

  /*- 1 user có nhiều thông báo -*/
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private List<Notification> notifications = new ArrayList<>();
}
