package org.cosmic.backend.domain.mail;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.cosmic.backend.domain.mail.applications.EmailService;
import org.cosmic.backend.domain.mail.exceptions.IntervalNotEnoughException;
import org.cosmic.backend.domain.mail.utils.AuthCodeMailSender;
import org.cosmic.backend.domain.user.domains.Email;
import org.cosmic.backend.domain.user.exceptions.NotExistEmailException;
import org.cosmic.backend.domain.user.repositorys.EmailRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@Log4j2
class EmailServiceTest {

  @InjectMocks
  private EmailService emailService;

  @Mock
  private EmailRepository emailRepository;
  @Mock
  private AuthCodeMailSender authCodeMailSender;

  @Test
  @DisplayName("이메일 인증 요청 시 메일이 존재하지 않는 경우 이메일 레코드 생성")
  public void alreadyExistEmailTest() {
    String to = "test@example.com";
    Email email = Email.from(to);
    when(emailRepository.findById(to)).thenReturn(Optional.empty());
    when(emailRepository.save(any())).thenReturn(email);

    emailService.sendVerificationEmail(to);

    verify(emailRepository).save(any());
  }

  @Test
  @DisplayName("이메일 인증 요청 시 메일이 존재하는 경우 이메일 레코드 업데이트")
  public void verifyEmailBeforeInterval() {
    String to = "test@example.com";
    Email email = Email.from(to);
    email.setCreateTime(Instant.now().minus(31, ChronoUnit.SECONDS));
    String defaultCode = email.getVerificationCode();
    when(emailRepository.findById(to)).thenReturn(Optional.of(email));

    emailService.sendVerificationEmail(to);

    verify(authCodeMailSender).verificationMessage(email);
    Assertions.assertNotEquals(defaultCode, email.getVerificationCode());
  }

  @Test
  @DisplayName("이메일 인증 요청 시간 텀이 너무 짧은 경우")
  public void verifyEmailAfterInterval() {
    String to = "test@example.com";
    Email email = Email.from(to);
    email.setCreateTime(Instant.now().minus(29, ChronoUnit.SECONDS));
    when(emailRepository.findById(to)).thenReturn(Optional.of(email));

    Assertions.assertThrows(IntervalNotEnoughException.class,
        () -> emailService.sendVerificationEmail(to));
  }

  @Test
  @DisplayName("이메일 인증 코드 검증 실패")
  public void verifyCodeSuccess() {
    String to = "test@example.com";
    Email email = Email.from(to);
    when(emailRepository.findById(to)).thenReturn(Optional.of(email));

    Assertions.assertThrows(IllegalArgumentException.class,
        () -> emailService.verifyCode(to, "000"));
  }

  @Test
  @DisplayName("이메일 인증 코드 검증 성공")
  public void verifyCodeFail() {
    String to = "test@example.com";
    Email email = Email.from(to);
    when(emailRepository.findById(to)).thenReturn(Optional.of(email));

    emailService.verifyCode(to, email.getVerificationCode());

    Assertions.assertEquals(true, email.getVerified());
  }

  @Test
  @DisplayName("이메일 인증 시 유저가 없는 경우")
  public void verifyCodeNotExistEmail() {
    String to = "test@example.com";
    when(emailRepository.findById(to)).thenReturn(Optional.empty());
    Assertions.assertThrows(NotExistEmailException.class,
        () -> emailService.verifyCode(to, "000"));
  }
}