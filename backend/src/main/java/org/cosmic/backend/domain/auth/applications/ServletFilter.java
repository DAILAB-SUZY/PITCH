package org.cosmic.backend.domain.auth.applications;

import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.cosmic.backend.domain.auth.dto.UserLogin;
import org.cosmic.backend.domain.user.domain.User;
import org.springframework.util.StringUtils;

import java.io.IOException;

public class ServletFilter extends HttpFilter {
    private TokenProvider tokenProvider;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //초기화 코드
    }
    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        try{
            final String token = parseBearerToken(request);

            if(token!=null&&token.equalsIgnoreCase("null")){
                //userid가져오기. 위조된 경우 예외
                String userId=tokenProvider.validateAndGetUserId(token);

                //ServletFilter실행
                chain.doFilter(request,response);
            }
        }
        catch(Exception e){
            //예외 발생시 response를 403 forbidden으로 설정
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
    }

    private String parseBearerToken(HttpServletRequest request){
        //Http요청의 헤더를 파싱해 Bearer토큰을 리턴한다.
        String bearerToken=request.getHeader("Authorization");

        if(StringUtils.hasText(bearerToken)&&bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        }
        return null;
    }
    @Override
    public void destroy() {
        // 종료 코드
    }
}
