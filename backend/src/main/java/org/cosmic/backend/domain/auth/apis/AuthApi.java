package org.cosmic.backend.domain.auth.apis;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.log4j.Log4j2;
import org.cosmic.backend.domain.auth.dtos.UserLoginDetail;
import org.cosmic.backend.domain.auth.exceptions.CredentialNotMatchException;
import org.cosmic.backend.domain.user.applications.UserService;
import org.cosmic.backend.globals.annotations.ApiCommonResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * AuthApi는 사용자 인증과 관련된 API 엔드포인트를 제공합니다.
 */
@Log4j2
@RestController
@RequestMapping("/api/")
@ApiCommonResponses
public class AuthApi {
    private final UserService userService;

    /**
     * AuthApi 생성자.
     *
     * @param userService 사용자 서비스 객체
     */
    public AuthApi(UserService userService) {
        this.userService = userService;
    }

    /**
     * 사용자 인증을 수행하여 로그인 토큰을 반환합니다.
     *
     * @param userLogin 사용자 로그인 정보 DTO
     * @return ResponseEntity 로그인 성공 시 사용자 정보를 포함한 응답
     *
     * @throws CredentialNotMatchException 사용자를 찾을 수 없을 때 발생합니다.
     */
    @PostMapping("/auth/signin")
    @ApiResponse(responseCode = "401", description = "Email or Password is invalid")
    public ResponseEntity<?> authenticate(@RequestBody UserLoginDetail userLogin) {
        return ResponseEntity.ok(userService.getByCredentials(userLogin.getEmail(), userLogin.getPassword()));
    }


    /**
     * 리프레시 토큰을 이용해 사용자 정보를 반환합니다.
     *
     * @param refreshToken 리프레시 토큰
     * @return ResponseEntity 리프레시 토큰을 통해 인증된 사용자의 로그인 정보를 포함한 정보 생성 성공 응답
     *
     * @throws CredentialNotMatchException 리프레시 토큰이 유효하지 않거나 일치하지 않을 때 발생합니다.
     */
    @PostMapping("/auth/reissued")
    @ApiResponse(responseCode = "401", description = "RefreshToken is invalid")
    public ResponseEntity<?> reissued(@RequestHeader("Refresh-Token") String refreshToken) {
        return ResponseEntity.ok(userService.getUserByRefreshToken(refreshToken));
    }
}
