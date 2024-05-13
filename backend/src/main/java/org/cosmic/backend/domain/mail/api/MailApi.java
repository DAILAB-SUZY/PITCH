package org.cosmic.backend.domain.mail.api;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.cosmic.backend.domain.mail.application.EmailService;
import org.cosmic.backend.domain.mail.dto.EmailAddress;
import org.cosmic.backend.domain.mail.dto.VerificationForm;
import org.cosmic.backend.globals.dto.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mail")
public class MailApi {

    private final EmailService emailService;

    @PostMapping("/request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Ok",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = EmailAddress.class))
                    }),
            @ApiResponse(responseCode = "401",
                    description = "Email is already exist or interval is too short",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))
                    })
    }
    )
    public ResponseEntity<EmailAddress> sendVerificationEmail(@Valid @RequestBody EmailAddress address) {
        emailService.sendVerificationEmail(address.email(), "123456");
        return ResponseEntity.ok(address);
    }

    @PostMapping("/verify")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Ok",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = EmailAddress.class))
                    }),
            @ApiResponse(responseCode = "401",
                    description = "Email is not exist or random code is not matched",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))
                    })
    }
    )
    public ResponseEntity<EmailAddress> verifyEmail(@Valid @RequestBody VerificationForm form) {
        return emailService.verifyCode(form.email(), form.code());
    }
}
