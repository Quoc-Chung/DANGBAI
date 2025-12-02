package com.quocchung.dangbai.duandangbai.controller;


import static com.quocchung.dangbai.duandangbai.contants.ApiConstant.APPROVE_POST;
import static com.quocchung.dangbai.duandangbai.contants.ApiConstant.CREATE_POST;
import static com.quocchung.dangbai.duandangbai.contants.ApiConstant.DETAIL_POST;
import static com.quocchung.dangbai.duandangbai.contants.ApiConstant.FILTER_POST;
import static com.quocchung.dangbai.duandangbai.contants.ApiConstant.POST;

import com.quocchung.dangbai.duandangbai.dtos.request.ApprovePostRequest;
import com.quocchung.dangbai.duandangbai.dtos.request.CreatePostRequest;
import com.quocchung.dangbai.duandangbai.dtos.request.PostFilterRequest;
import com.quocchung.dangbai.duandangbai.dtos.response.ApiResponse;
import com.quocchung.dangbai.duandangbai.dtos.response.PageResponse;
import com.quocchung.dangbai.duandangbai.dtos.response.PostResponse;
import com.quocchung.dangbai.duandangbai.service.IPostService;
import com.quocchung.dangbai.duandangbai.service.JwtService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(POST)
@RequiredArgsConstructor
@Slf4j
public class PostController {

  private final IPostService postService;
  private final JwtService jwtService;

  @PostMapping( value = CREATE_POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ApiResponse<PostResponse> createPost(
      @Valid @RequestPart("post") CreatePostRequest request,
      @RequestPart("images") List<MultipartFile> images,
      @RequestHeader("Authorization") String authHeader
      ) {

    String token = authHeader.substring(7);
    // Lấy userId từ Authentication (Spring Security)
    Long userId = jwtService.extractUserId(token);
    log.info(" User Id của người dùng  : "+ userId.toString()) ;

    PostResponse response = postService.createPost(request, images, userId);
    return ApiResponse.success("Tạo bài đăng thành công", response);
  }

  /**
   * Duyệt hoặc từ chối bài đăng
   * ADMIN/STAFF ONLY
   */
  @PostMapping(APPROVE_POST)
  @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
  public ApiResponse<PostResponse> approvePost(
      @Valid @RequestBody ApprovePostRequest request,
      @RequestHeader("Authorization") String authHeader) {

    String token = authHeader.substring(7);
    Long adminId = jwtService.extractUserId(token);
    log.info("Admin {} đang xử lý bài đăng {}", adminId, request.getPostId());

    PostResponse response = postService.approvePost(request, adminId);

    String message = request.getApproved()
        ? "Đã duyệt bài đăng thành công"
        : "Đã từ chối bài đăng";

    return ApiResponse.success(message, response);
  }

  /**
   * Lấy danh sách bài đăng theo trạng thái (PENDING/APPROVED/REJECTED)
   * ADMIN/STAFF ONLY
   *
   * @param status - PENDING, APPROVED, REJECTED (optional - nếu null thì lấy tất cả)
   * @param page - Trang (default: 0)
   * @param size - Số bài đăng mỗi trang (default: 20)
   * @param sortBy - Sắp xếp theo field (default: createdAt)
   * @param sortDirection - ASC hoặc DESC (default: DESC)
   */
  @GetMapping(FILTER_POST)
  public ApiResponse<PageResponse<PostResponse>> getPostsByStatus(
      @RequestParam(required = false) String status,
      @RequestParam(defaultValue = "0") Integer page,
      @RequestParam(defaultValue = "20") Integer size,
      @RequestParam(defaultValue = "createdAt") String sortBy,
      @RequestParam(defaultValue = "DESC") String sortDirection) {

    PostFilterRequest request = PostFilterRequest.builder()
        .status(status)
        .page(page)
        .size(size)
        .sortBy(sortBy)
        .sortDirection(sortDirection)
        .build();

    PageResponse<PostResponse> pageResponse = postService.getPostsByStatus(request);

    return ApiResponse.successPage("Lấy danh sách bài đăng thành công", pageResponse);
  }
  /**
   * Lấy chi tiết bài đăng theo ID
   * PUBLIC - Không cần đăng nhập
   */
  @GetMapping(DETAIL_POST)
  public ApiResponse<PostResponse> getPostById(@PathVariable Long postId) {
    PostResponse response = postService.getPostById(postId);
    return ApiResponse.success("Lấy chi tiết bài đăng thành công", response);
  }

  @GetMapping("/lstpost")
    public ApiResponse<PageResponse<PostResponse>> getPostsByStatus(
            @RequestHeader("Authorization") String header,
            @RequestParam(required = true) String status ,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size){
      String token = header.substring(7);
      Long userId = jwtService.extractUserId(token);
      PageResponse<PostResponse> pageResponse = postService.getPostsByStatus( userId,status, page, size);
      return ApiResponse.success("Lấy thông tin thành công",  pageResponse);
  }

}

