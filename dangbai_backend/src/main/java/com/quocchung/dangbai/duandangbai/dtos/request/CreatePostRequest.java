package com.quocchung.dangbai.duandangbai.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePostRequest {
  @NotBlank(message = "Tiêu đề không được để trống")
  @Size(max = 255, message = "Tiêu đề không được vượt quá 255 ký tự")
  private String title;

  @NotBlank(message = "Mô tả không được để trống")
  @Size(max = 5000, message = "Mô tả không được vượt quá 5000 ký tự")
  private String description;

  @NotNull(message = "Giá không được để trống")
  @Positive(message = "Giá phải lớn hơn 0")
  private Double price;

  @NotBlank(message = "Địa điểm không được để trống")
  private String location;

  private Long categoryId;
}
