package org.cosmic.backend.domain.auth.apis;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.log4j.Log4j2;
import org.cosmic.backend.domain.auth.dtos.UserLogin;
import org.cosmic.backend.domain.user.applications.UserService;
import org.cosmic.backend.globals.annotations.ApiCommonResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequestMapping("/auth")
@ApiCommonResponses
public class AuthApi {
    private final UserService userService;

    public AuthApi(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signin")
    @ApiResponse(responseCode = "401", description = "Email or Password is invalid")
    public ResponseEntity<?> authenticate(@RequestBody UserLogin userLogin) {
        return ResponseEntity.ok(userService.getByCredentials(userLogin.getEmail(), userLogin.getPassword()));
    }

    @PostMapping("/reissued")
    @ApiResponse(responseCode = "401", description = "RefreshToken is invalid")
    public ResponseEntity<?> reissued(@RequestHeader("Refresh-Token") String refreshToken) {
        return ResponseEntity.ok(userService.getUserByRefreshToken(refreshToken));
    }
}
