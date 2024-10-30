package org.cosmic.backend.domain.mail;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.cosmic.backend.domain.mail.applications.EmailService;
import org.cosmic.backend.domain.mail.utils.MailContentGenerator;
import org.cosmic.backend.domain.user.domains.Email;
import org.cosmic.backend.domain.user.repositorys.EmailRepository;
import org.cosmic.backend.domain.user.repositorys.UsersRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailSender;

@ExtendWith(MockitoExtension.class)
@Log4j2
class EmailServiceTest {

  @InjectMocks
  private EmailService emailService;

  @Mock
  private EmailRepository emailRepository;

  @Mock
  private MailSender mailSender;

  @Mock
  private UsersRepository usersRepository;

  @Mock
  private MailContentGenerator mailContentGenerator;

  @Test
  @DisplayName("이메일 인증 요청 시 메일이 존재하지 않는 경우 이메일 레코드 생성")
  public void alreadyExistEmailTest() {
    String to = "test@example.com";
    Email email = Email.builder().email(to).verificationCode("123456").build();
    when(emailRepository.findById(to)).thenReturn(Optional.empty());
    when(emailRepository.save(any())).thenReturn(email);

    emailService.sendVerificationEmail(to);

    verify(emailRepository).save(any());
  }

  @Test
  public void verifyEmailBeforeInterval() {

  }
}
