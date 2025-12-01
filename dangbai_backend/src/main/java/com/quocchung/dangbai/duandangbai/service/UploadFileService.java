package com.quocchung.dangbai.duandangbai.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class UploadFileService {

  @Value("${file.upload-dir}")
  private String uploadDir;

  public String saveFile(MultipartFile file, Long postId) {
    try {
      String originalName = file.getOriginalFilename();
      String extension = originalName.substring(originalName.lastIndexOf('.'));

      // Tên file an toàn + unique
      String fileName = System.currentTimeMillis() + "_" + UUID.randomUUID() + extension;

      // Tạo thư mục uploads/posts/{postId}
      Path uploadPath = Paths.get(uploadDir, "posts", postId.toString());
      Files.createDirectories(uploadPath);

      // Đường dẫn file cuối
      Path filePath = uploadPath.resolve(fileName);

      // Lưu file
      Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

      // Trả về URL public
      return "/uploads/posts/" + postId + "/" + fileName;

    } catch (Exception e) {
      log.error("Lỗi upload file: {}", e.getMessage());
      throw new RuntimeException("Không thể upload file");
    }
  }


  public boolean deleteFile(String urlPath) {
    try {
      Path filePath = Paths.get(urlPath.replace("/uploads", "uploads"));
      return Files.deleteIfExists(filePath);
    } catch (Exception e) {
      log.error("Không thể xóa file: {}", urlPath);
      return false;
    }
  }
}
