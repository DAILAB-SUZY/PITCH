package org.cosmic.backend.domain.mail.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.cosmic.backend.domain.mail.application.EmailService;
import org.cosmic.backend.domain.mail.dto.EmailAddress;
import org.cosmic.backend.domain.mail.dto.VerificationForm;
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
    public void sendVerificationEmail(@Valid @RequestBody EmailAddress address) {
        emailService.sendVerificationEmail(address.email(), "123456");
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyEmail(@Valid @RequestBody VerificationForm form) {
        return emailService.verifyCode(form.email(), form.code());
    }
}
