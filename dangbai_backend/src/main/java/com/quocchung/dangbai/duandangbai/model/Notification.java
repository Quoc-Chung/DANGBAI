package com.quocchung.dangbai.duandangbai.model;

import com.quocchung.dangbai.duandangbai.utils.enums.NotificationType;
import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Notification {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /*- Mỗi một thông báo sẽ liên quan đến 1 người dùng -*/
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  /*- Mỗi một thông báo sẽ có thể liên quan đến 1 comment -*/
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "comment_id")
  private Comment comment;

  /*- Một thông báo sẽ liên quan đến 1 cảm xúc -*/
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "reaction_id")
  private Reaction reaction;


  @Enumerated(EnumType.STRING)
  @Column(length = 500)
  private NotificationType type;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "post_id")
  private Post post;

  private String message;

  private Boolean isRead = false;

  @CreatedDate
  private LocalDateTime createdAt;
}
