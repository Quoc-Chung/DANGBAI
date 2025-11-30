package com.quocchung.dangbai.duandangbai.repository;

import com.quocchung.dangbai.duandangbai.model.PostMedia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostMediaRepository extends JpaRepository<PostMedia, Long> {
}
