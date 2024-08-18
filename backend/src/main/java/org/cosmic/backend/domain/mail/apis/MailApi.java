package org.cosmic.backend.domain.mail.apis;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.cosmic.backend.domain.mail.applications.EmailService;
import org.cosmic.backend.domain.mail.dtos.EmailAddress;
import org.cosmic.backend.domain.mail.dtos.VerificationForm;
import org.cosmic.backend.globals.annotations.ApiCommonResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mail")
@ApiCommonResponses
public class MailApi {

    private final EmailService emailService;

    @PostMapping("/request")
    @ApiResponse(responseCode = "401", description = "Email is already exist or interval is too short")
    public ResponseEntity<EmailAddress> sendVerificationEmail(@Valid @RequestBody EmailAddress address) {
        emailService.sendVerificationEmail(address.email(), "123456");
        return ResponseEntity.ok(address);
    }

    @PostMapping("/verify")
    @ApiResponse(responseCode = "401", description = "Email is not exist or random code is not matched")
    public ResponseEntity<EmailAddress> verifyEmail(@Valid @RequestBody VerificationForm form) {
        return emailService.verifyCode(form.email(), form.code());
    }
}
