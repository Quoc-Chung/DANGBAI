package com.quocchung.dangbai.duandangbai.service.impl;

import com.quocchung.dangbai.duandangbai.dtos.request.ApprovePostRequest;
import com.quocchung.dangbai.duandangbai.dtos.request.CreatePostRequest;
import com.quocchung.dangbai.duandangbai.dtos.request.PostFilterRequest;
import com.quocchung.dangbai.duandangbai.dtos.response.PageResponse;
import com.quocchung.dangbai.duandangbai.dtos.response.PostResponse;
import com.quocchung.dangbai.duandangbai.exception.AppException;
import com.quocchung.dangbai.duandangbai.exception.CategoryException;
import com.quocchung.dangbai.duandangbai.exception.ErrorCode;
import com.quocchung.dangbai.duandangbai.exception.UserException;
import com.quocchung.dangbai.duandangbai.model.Category;
import com.quocchung.dangbai.duandangbai.model.Post;
import com.quocchung.dangbai.duandangbai.model.PostMedia;
import com.quocchung.dangbai.duandangbai.model.User;
import com.quocchung.dangbai.duandangbai.repository.CategoryRepository;
import com.quocchung.dangbai.duandangbai.repository.PostMediaRepository;
import com.quocchung.dangbai.duandangbai.repository.PostRepository;
import com.quocchung.dangbai.duandangbai.repository.UserRepository;
import com.quocchung.dangbai.duandangbai.service.ICloudinaryService;
import com.quocchung.dangbai.duandangbai.service.IPostService;
import com.quocchung.dangbai.duandangbai.service.NotificationService;
import com.quocchung.dangbai.duandangbai.utils.enums.MediaType;
import com.quocchung.dangbai.duandangbai.utils.enums.PostStatus;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostServiceImpl implements IPostService {

  private final UserRepository userRepository;
  private final CategoryRepository categoryRepository;
  private final PostRepository postRepository;
  private final ICloudinaryService cloudinaryService;
  private final PostMediaRepository postMediaRepository;
  private final NotificationService notificationService;

  @Override
  public PostResponse createPost(CreatePostRequest request, List<MultipartFile> images,
      Long userId) {

    // Trích xuất tác giả
    User author = userRepository.findById(userId).orElseThrow(() -> new UserException(
       ErrorCode.USER_NOT_FOUND));

    Category category = null;
    if(request.getCategoryId() != null){
      category = categoryRepository.findById(request.getCategoryId()).orElseThrow(() -> new CategoryException(
          ErrorCode.CATEGORY_NOT_FOUND
      ));
    }
    validateImages(images);

    Post post = Post.builder()
        .author(author)
        .category(category)
        .title(request.getTitle())
        .description(request.getDescription())
        .price(request.getPrice())
        .location(request.getLocation())
        .status(PostStatus.PENDING)
        .build();
    postRepository.save(post);

    List<PostMedia> mediaList = uploadAndCreateMedia(images, post);
    postMediaRepository.saveAll(mediaList);

    post.setMedia(mediaList);
    // Tạo thông báo
    notificationService.notifyPostCreate(post);

    log.info("Tạo bài đăng thành công - ID: {}, Số ảnh: {}", post.getId(), mediaList.size());
    return mapToPostResponse(post);

  }

  private void validateImages(List<MultipartFile> images) {
    if(images == null  && images.isEmpty()){
      throw new AppException(ErrorCode.FILE_NOT_FOUND);
    }
    if (images.size() > 10) {
      throw new AppException(ErrorCode.TOO_MANY_MEDIA_FILES);
    }
  }

  /**
   * Upload ảnh song song và tạo PostMedia entities
   */
  private List<PostMedia> uploadAndCreateMedia(List<MultipartFile> images, Post post) {
    // Upload tất cả ảnh song song
    List<CompletableFuture<Map<String, Object>>> uploadFutures = images.stream()
        .map(cloudinaryService::uploadImageAsync)
        .collect(Collectors.toList());

    // Đợi tất cả upload hoàn thành
    CompletableFuture.allOf(uploadFutures.toArray(new CompletableFuture[0])).join();

    // Tạo PostMedia entities từ kết quả upload
    List<PostMedia> mediaList = new ArrayList<>();

    for (int i = 0; i < uploadFutures.size(); i++) {
      try {
        Map<String, Object> uploadResult = uploadFutures.get(i).get();

        PostMedia media = PostMedia.builder()
            .post(post)
            .type(MediaType.IMAGE)
            .url((String) uploadResult.get("secure_url"))
            .thumbnailUrl(getEagerUrl(uploadResult))
            .width((Integer) uploadResult.get("width"))
            .height((Integer) uploadResult.get("height"))
            .position(i)
            .build();

        mediaList.add(media);

      } catch (Exception e) {
        log.error("Lỗi lấy kết quả upload: {}", e.getMessage());
        // Rollback: xóa các ảnh đã upload
        rollbackUploadedImages(mediaList);
        throw new AppException(ErrorCode.FILE_UPLOAD_FAILED);
      }
    }

    return mediaList;
  }

  /**
   * Lấy URL thumbnail từ eager transformation
   */
  private String getEagerUrl(Map<String, Object> uploadResult) {
    List<Map<String, Object>> eager =
        (List<Map<String, Object>>) uploadResult.get("eager");
    if (eager != null && !eager.isEmpty()) {
      return (String) eager.get(0).get("secure_url");
    }
    return (String) uploadResult.get("secure_url");
  }

  /**
   * Rollback: Xóa các ảnh đã upload khi có lỗi
   */
  private void rollbackUploadedImages(List<PostMedia> mediaList) {
    mediaList.forEach(media -> {
      String publicId = cloudinaryService.extractPublicId(media.getUrl());
      if (publicId != null) {
        cloudinaryService.deleteImage(publicId);
      }
    });
  }
  /**
   * Phê duyệt bài viết dành cho admin
   * @param request
   * @param approveId Id người duyệt
   * @return
   */
  @Override
  public PostResponse approvePost(ApprovePostRequest request,Long approveId) {

    Post post = postRepository.findById(request.getPostId())
        .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));

    // 3. Cập nhật trạng thái
    if (request.getApproved()) {
      post.setStatus(PostStatus.APPROVED);
      post.setApprovedAt(LocalDateTime.now());
      post.setRejectedReason(null);
      log.info("Admin {} đã duyệt bài đăng {}", approveId, post.getId());
    } else {
      post.setStatus(PostStatus.REJECTED);
      post.setRejectedReason(request.getRejectedReason());
      post.setApprovedAt(null);
      log.info("Admin {} đã từ chối bài đăng {}: {}",
          approveId, post.getId(), request.getRejectedReason());
    }
    post = postRepository.save(post);
    // TODO: Gửi thông báo cho tác giả
    notificationService.notifyPostApproved(post);
    return mapToPostResponse(post);
  }

  /**
   * Lấy ra danh sách bài viết theo trạng thái của chúng
   * @param request
   * @return
   */
  @Override
  @Transactional(readOnly = true)
  public PageResponse<PostResponse> getPostsByStatus(PostFilterRequest request) {
    PostStatus status = null;
    if (request.getStatus() != null && !request.getStatus().isEmpty()) {
      try {
        status = PostStatus.valueOf(request.getStatus().toUpperCase());
      } catch (IllegalArgumentException e) {
        throw new AppException(ErrorCode.INVALID_FORMAT);
      }
    }

    Sort sort = "ASC".equalsIgnoreCase(request.getSortDirection())
        ? Sort.by(request.getSortBy()).ascending()
        : Sort.by(request.getSortBy()).descending();

    Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);

    Page<Post> postPage = status != null
        ? postRepository.findByStatus(status, pageable)
        : postRepository.findAll(pageable);

    List<PostResponse> content = postPage.getContent().stream()
        .map(this::mapToPostResponse)
        .collect(Collectors.toList());

    return PageResponse.<PostResponse>builder()
        .items(content)
        .page(postPage.getNumber())
        .size(postPage.getSize())
        .totalItems(postPage.getTotalElements())
        .totalPages(postPage.getTotalPages())
        .hasNext(postPage.hasNext())
        .hasPrevious(postPage.hasPrevious())
        .build();
  }

  @Override
  @Transactional(readOnly = true)
  public PageResponse<PostResponse> getApprovedPosts(Long userId, String statusStr, Integer page, Integer size) {
    PostStatus status = null;
    if (statusStr != null && !statusStr.isEmpty()) {
      try {
        status = PostStatus.valueOf(statusStr.toUpperCase());
      } catch (IllegalArgumentException e) {
        throw new AppException(ErrorCode.INVALID_FORMAT);
      }
    }

    Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

    Page<Post> postPage;

    if (userId != null && status != null) {
      postPage = postRepository.findByAuthorIdAndStatus(userId, status, pageable);
    } else if (userId != null) {
      postPage = postRepository.findByAuthorId(userId, pageable);
    } else if (status != null) {
      postPage = postRepository.findByStatus(status, pageable);
    } else {
      postPage = postRepository.findAll(pageable);
    }
    List<PostResponse> content = postPage.getContent().stream()
        .map(this::mapToPostResponse)
        .collect(Collectors.toList());

    return PageResponse.<PostResponse>builder()
        .items(content)
        .page(postPage.getNumber())
        .size(postPage.getSize())
        .totalItems(postPage.getTotalElements())
        .totalPages(postPage.getTotalPages())
        .hasNext(postPage.hasNext())
        .hasPrevious(postPage.hasPrevious())
        .build();
  }

  @Override
  @Transactional(readOnly = true)
  public PostResponse getPostById(Long postId) {
    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));

    return mapToPostResponse(post);
  }

  public  PostResponse mapToPostResponse(Post post) {
    if (post == null) return null;

    return PostResponse.builder()
        .id(post.getId())
        .title(post.getTitle())
        .description(post.getDescription())
        .price(post.getPrice())
        .location(post.getLocation())
        .status(post.getStatus())
        .rejectedReason(post.getRejectedReason())
        .createdAt(post.getCreatedAt())
        .updatedAt(post.getUpdatedAt())
        .approvedAt(post.getApprovedAt())
        .author(
            post.getAuthor() != null
                ? PostResponse.AuthorResponse.builder()
                .id(post.getAuthor().getId())
                .username(post.getAuthor().getUsername())
                .email(post.getAuthor().getEmail())
                .build()
                : null
        )
        .category(
            post.getCategory() != null
                ? PostResponse.CategoryResponse.builder()
                .id(post.getCategory().getId())
                .name(post.getCategory().getName())
                .build()
                : null
        )
        .media(
            post.getMedia() != null
                ? post.getMedia().stream()
                .map(media -> PostResponse.MediaResponse.builder()
                    .id(media.getId())
                    .type(media.getType() != null ? media.getType().name() : null)
                    .url(media.getUrl())
                    .thumbnailUrl(media.getThumbnailUrl())
                    .width(media.getWidth())
                    .height(media.getHeight())
                    .position(media.getPosition())
                    .build())
                .collect(Collectors.toList())
                : Collections.emptyList()
        )
        .totalViews(post.getNotifications() != null ? (long) post.getNotifications().size() : 0L)
        .totalComments(post.getComments() != null ? (long) post.getComments().size() : 0L)
        .build();
  }

}
