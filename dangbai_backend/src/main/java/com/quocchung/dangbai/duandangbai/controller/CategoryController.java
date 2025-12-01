package com.quocchung.dangbai.duandangbai.controller;

import static com.quocchung.dangbai.duandangbai.contants.ApiConstant.CATEGORY;
import static com.quocchung.dangbai.duandangbai.contants.ApiConstant.CATEGORY_ALL;

import com.quocchung.dangbai.duandangbai.dtos.response.ApiResponse;
import com.quocchung.dangbai.duandangbai.dtos.response.CategoryResponse;
import com.quocchung.dangbai.duandangbai.service.CategoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(CATEGORY)
@RequiredArgsConstructor
public class CategoryController {
  private final CategoryService categoryService;
  @GetMapping(CATEGORY_ALL)
  public ApiResponse<List<CategoryResponse>> getAll(){
    List<CategoryResponse> categoryResponses = categoryService.getAll();
    return ApiResponse.success("Lấy danh mục sản phẩm thành công", categoryResponses);
  }

}
