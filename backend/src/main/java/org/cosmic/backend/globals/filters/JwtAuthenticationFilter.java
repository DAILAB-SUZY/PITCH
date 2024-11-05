package org.cosmic.backend.globals.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.cosmic.backend.domain.auth.applications.TokenProvider;
import org.cosmic.backend.globals.dto.ErrorCode;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
@Log4j2
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  public static final String HEADER_KEY = "Authorization";
  public static final String HEADER_VALUE_PREFIX = "Bearer ";
  private static final Long NONE_TOKEN = 0L;
  private final TokenProvider tokenProvider;

  protected Optional<String> obtainAccessToken(HttpServletRequest request) {
    String bearerToken = request.getHeader(HEADER_KEY);
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(HEADER_VALUE_PREFIX)) {
      return Optional.of(bearerToken.substring(HEADER_VALUE_PREFIX.length()));
    }
    return Optional.empty();
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    try {
      doFilter(request, response, filterChain);
    } catch (JwtException e) {
      setErrorResponse(response, ErrorCode.INVALID_TOKEN);
    }
  }

  private void doFilter(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException, JwtException {
    JwtAuthenticationToken authentication = createAuthenticationToken(request);
    if (authentication.getPrincipal().equals(NONE_TOKEN)) {
      filterChain.doFilter(request, response);
      return;
    }
    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    setSecurityContext(authentication);
    filterChain.doFilter(request, response);
  }

  private void setSecurityContext(JwtAuthenticationToken authentication) {
    SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
    securityContext.setAuthentication(authentication);
    SecurityContextHolder.setContext(securityContext);
  }

  private JwtAuthenticationToken createAuthenticationToken(HttpServletRequest request) {
    return obtainAccessToken(request).map(
            token -> JwtAuthenticationToken.authenticated(tokenProvider.validateAndGetLongId(token)))
        .orElse(JwtAuthenticationToken.NONE);
  }

  private void setErrorResponse(
      HttpServletResponse response,
      ErrorCode errorCode
  ) {
    ObjectMapper objectMapper = new ObjectMapper();
    response.setStatus(errorCode.getHttpStatus().value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    ErrorResponse errorResponse = new ErrorResponse(errorCode.getCode(), errorCode.getMessage());
    try {
      response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Data
  public static class ErrorResponse {

    private final Integer code;
    private final String message;
  }
}
