package com.quocchung.dangbai.duandangbai.repository;

import com.quocchung.dangbai.duandangbai.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}