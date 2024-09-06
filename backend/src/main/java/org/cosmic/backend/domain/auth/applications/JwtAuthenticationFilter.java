package org.cosmic.backend.domain.auth.applications;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JwtAuthenticationFilter는 요청의 헤더에서 JWT 토큰을 추출하고, 이를 검증하여 Spring Security의 인증 정보를 설정하는 필터입니다.
 *
 * 이 필터는 매 요청마다 실행되며, JWT 토큰을 사용하여 사용자의 인증을 처리합니다.
 *
 */
@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final TokenProvider tokenProvider;

    /**
     * JwtAuthenticationFilter 생성자.
     *
     * @param tokenProvider 토큰을 생성하고 검증하는 데 사용되는 TokenProvider 객체
     */
    public JwtAuthenticationFilter(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    /**
     * 요청을 필터링하여 JWT 토큰을 검증하고 인증 정보를 SecurityContext에 설정합니다.
     *
     * @param request  현재 요청을 나타내는 HttpServletRequest 객체
     * @param response 현재 응답을 나타내는 HttpServletResponse 객체
     * @param filterChain 요청 처리 체인
     *
     * @throws ServletException 요청 처리 중 예외가 발생한 경우
     * @throws IOException      입력/출력 오류가 발생한 경우
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //요청에서 토큰 가져오기
        String token=parseBearerToken(request);
        //토큰 검사하기 JWT이므로 인가 서버에 요청하지 않고도 검증 가능
        if(token!=null&&!token.equalsIgnoreCase("null")){
            //userID가져오기 위조된 경우 예외처리(다르다면)
            Long userId=Long.parseLong(tokenProvider.validateAndGetUserId(token));
            //인증 완료; secutiryContextHolder에 등록해야 인증된 사용자라 생각함
            AbstractAuthenticationToken authentication=new UsernamePasswordAuthenticationToken(
                    userId,
                    null,
                    AuthorityUtils.NO_AUTHORITIES
            );
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContext securityContext= SecurityContextHolder.createEmptyContext();
            securityContext.setAuthentication(authentication);
            SecurityContextHolder.setContext(securityContext);
        }

        filterChain.doFilter(request,response);
    }

    /**
     * 요청의 Authorization 헤더에서 Bearer 토큰을 추출합니다.
     *
     * @param request 현재 요청을 나타내는 HttpServletRequest 객체
     * @return 추출된 Bearer 토큰. Bearer 토큰이 없거나 형식이 올바르지 않은 경우 null 반환
     */
    private String parseBearerToken(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        }
        return null;
    }
}
