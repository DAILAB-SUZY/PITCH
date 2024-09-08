package org.cosmic.backend.domain.mail.apis;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.cosmic.backend.domain.mail.applications.EmailService;
import org.cosmic.backend.domain.mail.dtos.EmailAddress;
import org.cosmic.backend.domain.mail.dtos.VerificationForm;
import org.cosmic.backend.domain.mail.exceptions.ExistEmailException;
import org.cosmic.backend.domain.mail.exceptions.IntervalNotEnoughException;
import org.cosmic.backend.domain.user.exceptions.NotMatchPasswordException;
import org.cosmic.backend.globals.annotations.ApiCommonResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 이메일 관련 작업을 처리하는 REST 컨트롤러입니다.
 * 인증 이메일 요청 및 인증 코드를 통한 이메일 인증 기능을 제공합니다.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/")
@ApiCommonResponses
public class MailApi {

    private final EmailService emailService;

    /**
     * 제공된 이메일 주소로 인증 이메일을 전송합니다.
     *
     * @param address 인증 이메일을 보낼 이메일 주소
     * @return 전송된 이메일 주소를 포함한 {@link ResponseEntity}
     *
     * @throws ExistEmailException 이메일이 이미 존재하는 경우
     * @throws IntervalNotEnoughException 요청 간격이 너무 짧은 경우
     * @throws RuntimeException 기타 문제가 있는 경우
     */
    @PostMapping("/mail/request")
    @ApiResponse(responseCode = "401", description = "Email is already exist or interval is too short")
    public ResponseEntity<EmailAddress> sendVerificationEmail(@Valid @RequestBody EmailAddress address) {
        emailService.sendVerificationEmail(address.email(), "123456");
        return ResponseEntity.ok(address);
    }

    /**
     * 제공된 이메일 주소와 해당 인증 코드를 통해 이메일을 인증합니다.
     *
     * @param form 이메일 주소와 인증 코드를 포함한 폼 데이터
     * @return 인증된 이메일 주소를 포함한 {@link ResponseEntity}
     *
     * @throws NotMatchPasswordException 이메일이 존재하지 않거나 인증 코드가 일치하지 않는 경우
     */
    @PostMapping("mail/verify")
    @ApiResponse(responseCode = "401", description = "Email is not exist or random code is not matched")
    public ResponseEntity<EmailAddress> verifyEmail(@Valid @RequestBody VerificationForm form) {
        return emailService.verifyCode(form.email(), form.code());
    }
}
