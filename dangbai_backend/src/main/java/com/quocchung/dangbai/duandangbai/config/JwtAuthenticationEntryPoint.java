package com.quocchung.dangbai.duandangbai.config;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import java.io.IOException;
import com.quocchung.dangbai.duandangbai.dtos.response.ApiResponse;

@Component
@Slf4j
/**
 * Đây là một class dùng để xử lý lỗi khi người dùng truy cập API mà không có token hoặc token không hợp lệ.
 * Khi token sai → báo 401 Unauthorized
 * Khi token hết hạn → báo 401 Unauthorized
 * */
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

  @Override
  public void commence(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException authException
  ) throws IOException, ServletException {

    log.error("Unauthorized error: {}", authException.getMessage());

    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

    // Sử dụng ApiResponse thay vì ResponseData
    ApiResponse<Void> errorResponse = ApiResponse.errorWithHttpStatusAndPath(
        HttpStatus.UNAUTHORIZED.value(),
        "Bạn cần đăng nhập để truy cập tài nguyên này",
        request.getRequestURI()
    );

    final ObjectMapper mapper = new ObjectMapper();
    mapper.writeValue(response.getOutputStream(), errorResponse);
  }
}