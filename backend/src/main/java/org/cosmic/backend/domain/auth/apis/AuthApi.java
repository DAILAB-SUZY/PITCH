package org.cosmic.backend.domain.auth.apis;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import org.cosmic.backend.domain.auth.dtos.PasswordChangeForm;
import org.cosmic.backend.domain.auth.dtos.UserLoginDetail;
import org.cosmic.backend.domain.auth.exceptions.CredentialNotMatchException;
import org.cosmic.backend.domain.mail.applications.EmailService;
import org.cosmic.backend.domain.user.applications.UserService;
import org.cosmic.backend.domain.user.dtos.EmailForm;
import org.cosmic.backend.globals.annotations.ApiCommonResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * AuthApi는 사용자 인증과 관련된 API 엔드포인트를 제공합니다.
 */
@Log4j2
@RestController
@RequestMapping("/api/")
@ApiCommonResponses
@Tag(name = "유저 관련 API", description = "로그인 및 회원가입")
public class AuthApi {

  private final UserService userService;
  private final EmailService emailService;

  /**
   * AuthApi 생성자.
   *
   * @param userService 사용자 서비스 객체
   */
  public AuthApi(UserService userService, EmailService emailService) {
    this.userService = userService;
    this.emailService = emailService;
  }

  /**
   * 사용자 인증을 수행하여 로그인 토큰을 반환합니다.
   *
   * @param userLogin 사용자 로그인 정보 DTO
   * @return ResponseEntity 로그인 성공 시 사용자 정보를 포함한 응답
   * @throws CredentialNotMatchException 사용자를 찾을 수 없을 때 발생합니다.
   */
  @PostMapping("/auth/signin")
  @ApiResponse(responseCode = "401", description = "Email or Password is invalid")
  @ApiResponse(responseCode = "200", content = {
      @Content(schema = @Schema(contentMediaType = MediaType.APPLICATION_JSON_VALUE, implementation = UserLoginDetail.class))})
  @Operation(summary = "로그인 API", description = "사용자 정보로 로그인하여 JWT 토큰을 발행합니다.")
  public ResponseEntity<UserLoginDetail> authenticate(@RequestBody UserLoginDetail userLogin) {
    return ResponseEntity.ok(
        userService.getByCredentials(userLogin.getEmail(), userLogin.getPassword()));
  }

  /**
   * 리프레시 토큰을 이용해 사용자 정보를 반환합니다.
   *
   * @param refreshToken 리프레시 토큰
   * @return ResponseEntity 리프레시 토큰을 통해 인증된 사용자의 로그인 정보를 포함한 정보 생성 성공 응답
   * @throws CredentialNotMatchException 리프레시 토큰이 유효하지 않거나 일치하지 않을 때 발생합니다.
   */
  @PostMapping("/auth/reissued")
  @ApiResponse(responseCode = "401", description = "RefreshToken is invalid")
  @ApiResponse(responseCode = "200", content = {
      @Content(schema = @Schema(contentMediaType = MediaType.APPLICATION_JSON_VALUE, implementation = UserLoginDetail.class))})
  @Operation(summary = "토큰 갱신 API", description = "refresh-Token을 가지고 JWT 토큰을 재발행합니다.")
  public ResponseEntity<UserLoginDetail> reissued(
      @RequestHeader("Refresh-Token") String refreshToken) {
    return ResponseEntity.ok(userService.getUserByRefreshToken(refreshToken));
  }

  @PostMapping("/auth/passwordchange")
  @Operation(summary = "비밀번호 변경 제출 API", description = "유저의 비밀번호를 변경합니다.")
  public ResponseEntity<String> changePassword(@RequestBody PasswordChangeForm passwordChangeForm) {
    userService.changePassword(passwordChangeForm.password(), passwordChangeForm.authCode());
    return ResponseEntity.ok("");
  }

  @PostMapping("/auth/reset-request")
  @Operation(summary = "비밀번호 변경 요청 API", description = "유저 비밀번호 변경 링크를 전송합니다.")
  public ResponseEntity<String> changePasswordRequest(@RequestBody EmailForm emailForm) {
    userService.doesEmailExist(emailForm.email());
    emailService.sendPasswordChangeMail(emailForm.email());
    return ResponseEntity.ok("");
  }
}
