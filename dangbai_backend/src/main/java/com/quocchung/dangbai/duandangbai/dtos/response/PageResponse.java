package com.quocchung.dangbai.duandangbai.dtos.response;

import java.util.Collections;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageResponse<T> {
  private List<T> items;
  private int page;
  private int size;
  private long totalItems;
  private int totalPages;
  private boolean hasNext;
  private boolean hasPrevious;

  public static <T> PageResponse<T> empty() {
    return PageResponse.<T>builder()
        .items(Collections.emptyList())
        .page(0)
        .size(0)
        .totalItems(0)
        .totalPages(0)
        .hasNext(false)
        .hasPrevious(false)
        .build();
  }
}
