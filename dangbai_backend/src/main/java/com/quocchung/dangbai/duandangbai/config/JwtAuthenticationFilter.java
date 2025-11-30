package com.quocchung.dangbai.duandangbai.config;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quocchung.dangbai.duandangbai.dtos.response.ApiResponse;
import com.quocchung.dangbai.duandangbai.service.JwtService;
import com.quocchung.dangbai.duandangbai.service.TokenStorageService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final UserDetailsService userDetailsService;
  private final TokenStorageService tokenStorageService;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    final String authHeader = request.getHeader("Authorization");

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);
      return;
    }

    final String token = authHeader.substring(7).trim();
    String username;

    try {
      username = jwtService.extractUsername(token);
    } catch (ExpiredJwtException e) {
      log.error("Token expired: {}", e.getMessage());
      unauthorizedResponse(response, "Token đã hết hạn");
      return;
    } catch (JwtException e) {
      log.error("Token invalid: {}", e.getMessage());
      unauthorizedResponse(response, "Token không hợp lệ");
      return;
    }

    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

      UserDetails userDetails = userDetailsService.loadUserByUsername(username);

      boolean tokenValid = jwtService.isTokenValid(token, userDetails)
                           && tokenStorageService.isAccessTokenValid(token);

      if (tokenValid) {
        UsernamePasswordAuthenticationToken authToken =
            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
      } else {
        log.warn("Token expired or not in Redis");
        unauthorizedResponse(response, "Token không hợp lệ hoặc hết hạn");
        return;
      }
    }

    filterChain.doFilter(request, response);
  }

  private void unauthorizedResponse(HttpServletResponse response, String message) throws IOException {
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

    ApiResponse<Void> errorResponse = ApiResponse.errorWithHttpStatusAndPath(
        HttpServletResponse.SC_UNAUTHORIZED,
        message,
        ""
    );

    final ObjectMapper mapper = new ObjectMapper();
    mapper.writeValue(response.getOutputStream(), errorResponse);
  }
}
