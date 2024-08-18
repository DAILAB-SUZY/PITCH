package org.cosmic.backend.domain.user.apis;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.cosmic.backend.domain.user.applications.UserService;
import org.cosmic.backend.domain.user.dtos.JoinRequest;
import org.cosmic.backend.globals.annotations.ApiCommonResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@ApiCommonResponses
public class UserApi {

    private final UserService userService;

    public UserApi(UserService userService) {
        this.userService = userService;
    }

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
