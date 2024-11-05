package org.cosmic.backend.domain.mail;

import java.util.NoSuchElementException;
import org.cosmic.backend.AbstractContainerBaseTest;
import org.cosmic.backend.domain.user.domains.Email;
import org.cosmic.backend.domain.user.repositorys.EmailRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class EmailRepositoryTest extends AbstractContainerBaseTest {

  @Autowired
  private EmailRepository emailRepository;

  @Test
  @DisplayName("이메일 생성")
  void emailSave() {
    Email email = Email.builder().email("testman@example.com").verificationCode("123456").build();
    emailRepository.save(email);

    Email savedEmail = emailRepository.findById("testman@example.com").orElseThrow(
        NoSuchElementException::new);

    Assertions.assertEquals("testman@example.com", savedEmail.getEmail());
  }

  @Test
  @DisplayName("이메일 조회")
  public void emailFind() {
    Email email = Email.builder().email("testman@example.com").verificationCode("123456").build();
    emailRepository.save(email);

    Assertions.assertTrue(emailRepository.existsById("testman@example.com"));
  }
}