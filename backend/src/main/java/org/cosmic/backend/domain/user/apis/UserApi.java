package org.cosmic.backend.domain.user.apis;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.cosmic.backend.domain.user.applications.UserService;
import org.cosmic.backend.domain.user.dtos.JoinRequest;
import org.cosmic.backend.domain.user.dtos.UserDetail;
import org.cosmic.backend.domain.user.exceptions.NotExistEmailException;
import org.cosmic.backend.domain.user.exceptions.NotMatchPasswordException;
import org.cosmic.backend.globals.annotations.ApiCommonResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>사용자(User) 관련 API를 제공하는 REST 컨트롤러입니다.</p>
 *
 * <p>이 API는 사용자 등록 기능을 제공합니다.</p>
 */
@RestController
@RequestMapping("/api/")
@ApiCommonResponses
@Tag(name = "유저 관련 API", description = "로그인 및 회원가입")
@Log4j2
public class UserApi {

  private final UserService userService;

  /**
   * <p>UserApi의 생성자입니다.</p>
   *
   * @param userService 사용자 관련 비즈니스 로직을 처리하는 서비스 클래스
   */
  public UserApi(UserService userService) {
    this.userService = userService;
  }

  /**
   * <p>새로운 사용자를 등록합니다.</p>
   *
   * @param request 사용자 등록 요청 정보를 포함한 객체
   * @return 등록 성공 메시지를 포함한 {@link ResponseEntity}
   * @throws NotMatchPasswordException 비밀번호가 조건에 맞지 않거나 확인 비밀번호와 일치하지 않을 때 발생합니다.
   * @throws NotExistEmailException    이메일이 존재하지 않을 때 발생합니다.
   * @throws NullPointerException      요청 데이터가 NULL일 때 발생할 수 있습니다.
   * @throws IllegalArgumentException  요청 본문이 비어 있을 때 발생할 수 있습니다.
   */
  @PostMapping("/user")
  @ApiResponse(responseCode = "400", description = "Not met password condition or Not Match Password")
  @ApiResponse(responseCode = "404", description = "Not found email")
  @ApiResponse(responseCode = "406", description = "Read NULL data")
  @ApiResponse(responseCode = "415", description = "Request body is empty")
  @Operation(summary = "회원가입 API")
  public ResponseEntity<String> userRegister(@Valid @RequestBody JoinRequest request) {
    userService.userRegister(request);
    return ResponseEntity.ok("success for signup");
  }

  @GetMapping("me")
  @ApiResponse(responseCode = "200", description = "ok", content = @Content(schema = @Schema(implementation = UserDetail.class)))
  @Operation(summary = "로그인 유저 정보 API", description = "엑세스 토큰에 담겨 있는 유저의 정보를 조회합니다.")
  public ResponseEntity<UserDetail> userInfo(@AuthenticationPrincipal Long userId) {
    return ResponseEntity.ok(userService.getUserDetail(userId));
  }

  @PostMapping("/user/passwordchange/{authCode}")
  @Operation(summary = "비밀번호 변경 제출 API", description = "유저의 비밀번호를 변경합니다.")
  public ResponseEntity<String> changePassword(@PathVariable String authCode) {
    return ResponseEntity.ok("");
  }
}
