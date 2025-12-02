package com.quocchung.dangbai.duandangbai.repository;

import com.quocchung.dangbai.duandangbai.model.Post;
import com.quocchung.dangbai.duandangbai.utils.enums.PostStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
  // Lấy bài viết theo trạng thái
  Page<Post> findByStatus(PostStatus status, Pageable pageable);

  // Lấy bài viết theo user
  Page<Post> findByAuthorId(Long authorId, Pageable pageable);

  // Lấy bài viết theo user và trạng thái
  Page<Post> findByAuthorIdAndStatus(Long authorId, PostStatus status, Pageable pageable);


    @Query("SELECT p FROM Post p WHERE p.author.id = :userId AND p.status = :status")
    Page<Post> findByUserAndStatus(@Param("userId") Long userId,
                                   @Param("status") PostStatus status,
                                   Pageable pageable);

}