package com.quocchung.dangbai.duandangbai.service;

import com.quocchung.dangbai.duandangbai.dtos.request.ApprovePostRequest;
import com.quocchung.dangbai.duandangbai.dtos.request.CreatePostRequest;
import com.quocchung.dangbai.duandangbai.dtos.request.PostFilterRequest;
import com.quocchung.dangbai.duandangbai.dtos.response.PageResponse;
import com.quocchung.dangbai.duandangbai.dtos.response.PostResponse;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface IPostService {
  /**
   * Tạo bài đăng với nhiều ảnh
   */
  PostResponse createPost(CreatePostRequest request, List<MultipartFile> images, Long userId);

  /**
   * Duyệt hoặc từ chối bài đăng (ADMIN/STAFF)
   */
  PostResponse approvePost(ApprovePostRequest request, Long adminId);

  /**
   * Lấy danh sách bài đăng theo trạng thái (ADMIN/STAFF)
   */
  PageResponse<PostResponse> getPostsByStatus(PostFilterRequest request);


  /**
   * Lấy danh sách bài đăng chưa được duyệt hoặc đẵ duyệt của người dùng
   */
  PageResponse<PostResponse> getApprovedPosts(Long userId, String status,  Integer page, Integer size);


  /**
   * Lấy chi tiết bài đăng theo ID
   */
  PostResponse getPostById(Long postId);

}