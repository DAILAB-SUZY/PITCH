package org.cosmic.backend.domain.user.apis;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.cosmic.backend.domain.user.applications.UserService;
import org.cosmic.backend.domain.user.dtos.JoinRequest;
import org.cosmic.backend.domain.user.exceptions.NotExistEmailException;
import org.cosmic.backend.domain.user.exceptions.NotMatchPasswordException;
import org.cosmic.backend.globals.annotations.ApiCommonResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 사용자(User) 관련 API를 제공하는 REST 컨트롤러입니다.
 * 사용자 등록 기능을 제공합니다.
 */
@RestController
@RequestMapping("/user")
@ApiCommonResponses
public class UserApi {

    private final UserService userService;

    /**
     * UserApi의 생성자입니다.
     *
     * @param userService 사용자 관련 비즈니스 로직을 처리하는 서비스 클래스
     */
    public UserApi(UserService userService) {
        this.userService = userService;
    }

    /**
     * 새로운 사용자를 등록합니다.
     *
     * @param request 사용자 등록 요청 정보를 포함한 객체
     * @return 등록 성공 메시지를 포함한 {@link ResponseEntity}
     *
     * @throws NotMatchPasswordException 비밀번호가 조건에 맞지 않거나 확인 비밀번호와 일치하지 않을 때 발생합니다.
     * @throws NotExistEmailException 이메일이 존재하지 않을 때 발생합니다.
     * @throws NullPointerException 요청 데이터가 NULL일 때 발생할 수 있습니다.
     * @throws IllegalArgumentException 요청 본문이 비어 있을 때 발생할 수 있습니다.
     */
    @PostMapping("/register")
    @ApiResponse(responseCode = "400", description = "Not met password condition or Not Match Password")
    @ApiResponse(responseCode = "404", description = "Not found email")
    @ApiResponse(responseCode = "406", description = "Read NULL data")
    @ApiResponse(responseCode = "415", description = "Request body is empty")
    public ResponseEntity<?> userRegister(@Valid @RequestBody JoinRequest request) {
        userService.userRegister(request);
        return ResponseEntity.ok("성공");
    }
}
