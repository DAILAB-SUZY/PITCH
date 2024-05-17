package org.cosmic.backend.domain.auth.applications;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
    {

        try{
            //요청에서 토큰 가져오기
            String token=parseBearerToken(request);
            log.info("Filter is runnig");
            //토큰 검사하기 JWT이므로 인가 서버에 요청하지 않고도 검증 가능
            if(token!=null&&!token.equalsIgnoreCase("null")){
                //userID가져오기 위조된 경우 예외처리(다르다면)
                String userId=tokenProvider.validateAndGetUserId(token);
                log.info("User id is "+userId);
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
        }
        catch(Exception e){
            logger.error("could not set user authentication in security context",e);
        }
    }

    private String parseBearerToken(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        }
        return null;
    }
}
