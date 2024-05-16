package org.cosmic.backend.globals.jwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.cosmic.backend.domain.auth.dto.UserLogin;
import org.cosmic.backend.domain.login.PrincipalDetails;
import org.cosmic.backend.domain.user.domain.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import java.io.IOException;

public class JwtTokenFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;

    public JwtTokenFilter(JwtTokenUtil jwtTokenUtil, AuthenticationManager authenticationManager) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) throws AuthenticationException {
        //로그인 시도할 때 실행
        ObjectMapper objectMapper = new ObjectMapper();
        UserLogin user=null;

        try{
            user=objectMapper.readValue(req.getInputStream(), UserLogin.class);
            //request로 들어온 JSON형식을 userDto로 가져옴
        }catch(Exception e){
            throw new AuthenticationServiceException("Failed to parse authentication request body",e);
        }
        //토큰 생성
        UsernamePasswordAuthenticationToken token=new UsernamePasswordAuthenticationToken(user.getEmail(),user.getPassword());
        //authenticationManager의 authenticate메소드 실행
        Authentication authenticate = authenticationManager.authenticate(token);
        //authenticationManager는 처리할 수 있는 authenticationProvider를 찾아서 authenticationProvider의 authenticate 메소드 실행.
        return authenticate;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain, Authentication auth) throws IOException, ServletException {
        PrincipalDetails principal=(PrincipalDetails)auth.getPrincipal();
        User user=principal.getUser();
        String jwt=jwtTokenUtil.createJwt(user.getEmail().getEmail());
        res.setContentType("Access-Control-Expose-Headers","Authorization");
        res.setHeader("Authorization","Bearer " + jwt);
        //super.successfulAuthentication(req, res, chain, auth);
    }
}