package com.quocchung.dangbai.duandangbai.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
        .info(new Info()
            .title("API Cho Ứng Dụng Đăng Tin")
            .version("1.0.0")
            .description("Tài liệu API tự động được tạo bởi Swagger (springdoc-openapi)")
        );
  }
}