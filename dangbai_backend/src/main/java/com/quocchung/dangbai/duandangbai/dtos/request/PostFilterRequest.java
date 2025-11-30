package com.quocchung.dangbai.duandangbai.dtos.request;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostFilterRequest {

  private String status; // "PENDING", "APPROVED", "REJECTED"

  @Builder.Default
  private Integer page = 0;

  @Builder.Default
  private Integer size = 20;

  @Builder.Default
  private String sortBy = "createdAt";

  @Builder.Default
  private String sortDirection = "DESC";
}