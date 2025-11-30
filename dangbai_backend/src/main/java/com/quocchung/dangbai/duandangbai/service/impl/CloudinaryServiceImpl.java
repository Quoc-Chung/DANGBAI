package com.quocchung.dangbai.duandangbai.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.quocchung.dangbai.duandangbai.service.ICloudinaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class CloudinaryServiceImpl implements ICloudinaryService {

  private final Cloudinary cloudinary;

  @Override
  public CompletableFuture<Map<String, Object>> uploadImageAsync(MultipartFile file) {
    return CompletableFuture.supplyAsync(() -> {
      try {
        return uploadImage(file);
      } catch (IOException e) {
        log.error("Lỗi upload ảnh: {}", e.getMessage());
        throw new RuntimeException("Upload ảnh thất bại: " + e.getMessage());
      }
    });
  }

  @Override
  public Map<String, Object> uploadImage(MultipartFile file) throws IOException {
    validateImage(file);

    Map<String, Object> uploadParams = ObjectUtils.asMap(
        "folder", "DangBai",
        "resource_type", "auto",
        "transformation", new com.cloudinary.Transformation()
            .quality("auto:good")
            .fetchFormat("auto"),
        "eager", new com.cloudinary.Transformation[]{
            new com.cloudinary.Transformation()
                .width(300).height(300).crop("fill")
        }
    );

    Map<String, Object> result = cloudinary.uploader()
        .upload(file.getBytes(), uploadParams);

    log.info("Upload thành công: {}", result.get("secure_url"));
    return result;
  }

  @Override
  public void deleteImage(String publicId) {
    try {
      cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
      log.info("Đã xóa ảnh: {}", publicId);
    } catch (IOException e) {
      log.error("Lỗi xóa ảnh: {}", e.getMessage());
    }
  }

  @Override
  public String extractPublicId(String url) {
    if (url == null || url.isEmpty()) {
      return null;
    }
    // URL format: https://res.cloudinary.com/.../posts/abc123.jpg
    String[] parts = url.split("/");
    if (parts.length > 0) {
      String lastPart = parts[parts.length - 1];
      return "posts/" + lastPart.split("\\.")[0];
    }
    return null;
  }

  /**
   * Validate file ảnh
   */
  private void validateImage(MultipartFile file) {
    if (file.isEmpty()) {
      throw new IllegalArgumentException("File không được để trống");
    }

    // Kiểm tra kích thước (max 10MB)
    if (file.getSize() > 10 * 1024 * 1024) {
      throw new IllegalArgumentException("Kích thước file không được vượt quá 10MB");
    }

    // Kiểm tra định dạng
    String contentType = file.getContentType();
    if (contentType == null || !contentType.startsWith("image/")) {
      throw new IllegalArgumentException("File phải là định dạng ảnh");
    }
  }
}
