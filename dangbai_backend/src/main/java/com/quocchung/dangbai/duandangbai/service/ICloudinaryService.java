package com.quocchung.dangbai.duandangbai.service;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface ICloudinaryService {

  /**
   * Upload ảnh lên Cloudinary - Async
   */
  CompletableFuture<Map<String, Object>> uploadImageAsync(MultipartFile file);

  /**
   * Upload ảnh đồng bộ
   */
  Map<String, Object> uploadImage(MultipartFile file) throws IOException;

  /**
   * Xóa ảnh từ Cloudinary
   */
  void deleteImage(String publicId);

  /**
   * Lấy public_id từ URL Cloudinary
   */
  String extractPublicId(String url);
}
