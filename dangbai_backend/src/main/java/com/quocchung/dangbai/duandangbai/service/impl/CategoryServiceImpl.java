package com.quocchung.dangbai.duandangbai.service.impl;

import com.quocchung.dangbai.duandangbai.dtos.response.CategoryResponse;
import com.quocchung.dangbai.duandangbai.model.Category;
import com.quocchung.dangbai.duandangbai.repository.CategoryRepository;
import com.quocchung.dangbai.duandangbai.service.CategoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

  private final CategoryRepository categoryRepository;

  @Override
  public List<CategoryResponse> getAll() {
    return toCategoryResponse(categoryRepository.findAll());
  }

  public List<CategoryResponse> toCategoryResponse(List<Category> lstCategory){
    return lstCategory.stream()
        .map(x -> CategoryResponse.builder()
            .Id(x.getId())
            .name(x.getName())
            .build())
        .toList();
  }
}
