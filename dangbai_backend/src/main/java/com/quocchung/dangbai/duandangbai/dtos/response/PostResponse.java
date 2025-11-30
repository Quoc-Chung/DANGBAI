package com.quocchung.dangbai.duandangbai.dtos.response;

import com.quocchung.dangbai.duandangbai.utils.enums.PostStatus;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse {
  private Long id;
  private String title;
  private String description;
  private Double price;
  private String location;
  private PostStatus status;
  private String rejectedReason;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private LocalDateTime approvedAt;

  private AuthorResponse author;
  private CategoryResponse category;
  private List<MediaResponse> media;

  // tổng số lượt xem
  private Long totalViews;

  // tổng số comment
  private Long totalComments;

  @Data
  @Builder
  public static class AuthorResponse {
    private Long id;
    private String username;
    private String email;
  }

  @Data
  @Builder
  public static class CategoryResponse {
    private Long id;
    private String name;
  }

  @Data
  @Builder
  public static class MediaResponse {
    private Long id;
    private String type;
    private String url;
    private String thumbnailUrl;
    private Integer width;
    private Integer height;
    private Integer position;
  }
}
