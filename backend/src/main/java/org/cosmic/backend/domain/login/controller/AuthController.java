package org.cosmic.backend.domain.login.controller;

import org.cosmic.backend.domain.login.dto.LoginRequest;
import org.cosmic.backend.domain.user.domain.User;
import org.cosmic.backend.globals.jwtUtil.JwtTokenUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // REST 컨트롤러임을 선언
@RequestMapping("/api/auth") // 이 컨트롤러의 모든 메소드가 응답하는 기본 URI
public class AuthController {

    private final AuthenticationManager authenticationManager; // Spring Security의 인증 관리자
    private final JwtTokenUtil jwtTokenUtil; // JWT 토큰을 생성하고 검증하는 유틸리티 클래스
    private final PasswordEncoder passwordEncoder; // 비밀번호를 암호화하고 검증하는 인코더

    // 생성자 주입을 사용하여 필요한 컴포넌트들을 주입받음
    public AuthController(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login") // 로그인 API 엔드포인트
    public ResponseEntity<UserResponse> authenticateUser(@RequestBody LoginRequest loginRequest) {
        // 사용자의 이름과 비밀번호로 UsernamePasswordAuthenticationToken 객체를 생성
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        // Spring Security의 SecurityContext에 인증 정보를 설정함으로써, 이후 요청에 대한 인증 정보를 유지
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 인증 정보를 기반으로 JWT 토큰 생성
        String jwt = jwtTokenUtil.generateToken(authentication);

        // User 객체로부터 UserDetails를 구현한 Principal을 가져옴
        User user = (User) authentication.getPrincipal();

        // 생성된 JWT와 사용자 정보를 포함하여 클라이언트에 응답
        return ResponseEntity.ok(new UserResponse(user.getEmail(), jwt));
    }
}