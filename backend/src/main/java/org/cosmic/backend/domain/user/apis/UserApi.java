package org.cosmic.backend.domain.user.apis;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.cosmic.backend.domain.user.applications.UserService;
import org.cosmic.backend.domain.user.dtos.JoinRequest;
import org.cosmic.backend.globals.dto.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserApi {

    private final UserService userService;

    public UserApi(UserService userService) {
        this.userService = userService;
    }
    @PostMapping("/register")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
            description = "Ok",
            content = {
                @Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class))
            }),

        @ApiResponse(responseCode = "400",
            description = "Not met password condition or Not Match Password",
            content = {
                @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))
            }
        ),
        @ApiResponse(responseCode = "404",
            description = "Not found email",
            content = {
                @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))
            }
        ),
        @ApiResponse(responseCode = "406",
            description = "Read NULL data",
            content = {
                @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))
            }
        ),
        @ApiResponse(responseCode = "415",
            description = "Request body is empty",
            content = {
                @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))
            }
        )
    }
    )
    public ResponseEntity<?> userRegister(@Valid @RequestBody JoinRequest request) {
        userService.userRegister(request);
        return ResponseEntity.ok("성공");//회원가입 완료 표시
    }
}
