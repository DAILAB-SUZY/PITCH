package org.cosmic.backend.domain.mail.utils;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import lombok.RequiredArgsConstructor;
import org.cosmic.backend.domain.user.domains.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthCodeMailSender {

  private final JavaMailSender mailSender;
  private final String SUBJECT = "[PITCH] 회원 가입 인증";
  private final String REPLACED_STRING = "RANDOM_CODE";
  @Value("${spring.mail.username}")
  private String from;

  private String generateHtmlContent(String randomCode) {
    try {
      ClassPathResource resource = new ClassPathResource("templates/messages.html");
      String template = new String(Files.readAllBytes(Paths.get(resource.getURI())));
      return template.replace(REPLACED_STRING, randomCode);
    } catch (IOException e) {
      throw new RuntimeException("Failed to read email template", e);
    }
  }

  private MimeMessage verificationMessage(String to, String randomCode) throws MessagingException {
    MimeMessage mimeMessage = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
    helper.setText(generateHtmlContent(randomCode), true); // Use this or above line.
    helper.setTo(to);
    helper.setSubject(SUBJECT);
    helper.setFrom(from);
    return mimeMessage;
  }

  public void verificationMessage(Email email) {
    try {
      mailSender.send(verificationMessage(email.getEmail(), email.getVerificationCode()));
    } catch (MessagingException e) {
      throw new RuntimeException("Failed to send email", e);
    }
  }
}
