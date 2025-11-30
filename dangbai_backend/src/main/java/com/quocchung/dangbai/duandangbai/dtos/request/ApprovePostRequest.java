package com.quocchung.dangbai.duandangbai.dtos.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApprovePostRequest {

  @NotNull(message = "Post ID không được để trống")
  private Long postId;

  @NotNull(message = "Trạng thái không được để trống")
  private Boolean approved;

  private String rejectedReason;
}

