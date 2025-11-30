package com.quocchung.dangbai.duandangbai.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.quocchung.dangbai.duandangbai.utils.enums.MediaType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class PostMedia {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /* Mỗi media thuộc về 1 bài viết */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "post_id", nullable = false)
  @ToString.Exclude
  @JsonIgnore
  private Post post;

  @Enumerated(EnumType.STRING)
  private MediaType type;  // IMAGE, VIDEO, FILE

  private String url;            // Link ảnh đầy đủ
  private String thumbnailUrl;   // Link thumbnail (CDN resize)

  private Integer width;
  private Integer height;
  private Integer position;      // Thứ tự hiển thị

  @CreatedDate
  private LocalDateTime createdAt;
}
