package org.cosmic.backend.domain.mail.applications;

import java.util.Optional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.cosmic.backend.domain.mail.dtos.EmailAddress;
import org.cosmic.backend.domain.mail.exceptions.ExistEmailException;
import org.cosmic.backend.domain.mail.utils.AuthCodeMailSender;
import org.cosmic.backend.domain.user.domains.Email;
import org.cosmic.backend.domain.user.exceptions.NotExistEmailException;
import org.cosmic.backend.domain.user.exceptions.NotMatchPasswordException;
import org.cosmic.backend.domain.user.repositorys.EmailRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 이메일 관련 서비스를 제공하는 클래스입니다. 인증 이메일 전송 및 인증 코드 검증 기능을 포함합니다.
 */
@Service
@RequiredArgsConstructor
public class EmailService {

  private final AuthCodeMailSender authCodeMailSender;
  private final EmailRepository emailRepository;

  private void userExistCheck(Email email) {
    if (email.isUserExist()) {
      throw new ExistEmailException();
    }
  }

  @Transactional
  protected Email getOrUpdateVerification(String to) {
    Optional<Email> email = emailRepository.findById(to);
    if (email.isPresent()) {
      userExistCheck(email.get());
      email.get().updateVerificationCode();
      return email.get();
    }
    return emailRepository.save(Email.from(to));
  }

  /**
   * 주어진 이메일 주소로 인증 이메일을 전송합니다. (랜덤 코드)
   *
   * @param to 인증 이메일을 보낼 이메일 주소
   */
  @Transactional
  public void sendVerificationEmail(@NonNull String to) {
    Email email = getOrUpdateVerification(to);
    authCodeMailSender.verificationMessage(email);
  }

  /**
   * 주어진 이메일 주소와 인증 코드를 통해 이메일을 인증합니다.
   *
   * @param email 인증할 이메일 주소
   * @param code  사용자가 입력한 인증 코드
   * @return 인증된 이메일 주소를 포함한 {@link ResponseEntity}
   * @throws NotExistEmailException    이메일이 존재하지 않는 경우 발생합니다.
   * @throws NotMatchPasswordException 인증 코드가 일치하지 않는 경우 발생합니다.
   */
  @Transactional
  public ResponseEntity<EmailAddress> verifyCode(String email, String code) {
    Email verifingEmail = emailRepository.findById(email).orElseThrow(NotExistEmailException::new);
    verifingEmail.validVerificationCode(code);
    return ResponseEntity.ok(EmailAddress.from(verifingEmail));
  }
}
