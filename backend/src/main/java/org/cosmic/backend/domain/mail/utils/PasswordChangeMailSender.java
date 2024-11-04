package org.cosmic.backend.domain.mail.utils;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PasswordChangeMailSender {

  private final JavaMailSender mailSender;
  private final String SUBJECT = "[PITCH] 비밀번호 변경";
  private final String REPLACED_STRING = "PASSWORD_CHANGE_LINK";
  private final PasswordEncoder passwordEncoder;
  private final RedisTemplate<String, String> redisTemplate;

  @Value("${spring.mail.username}")
  private String from;
  @Value("${server.origin}")
  private String SERVER_ORIGIN;

  private String generateHtmlContent(String passwordChangeLink) {
    try {
      ClassPathResource resource = new ClassPathResource("templates/passwordChange.html");
      String template = new String(Files.readAllBytes(Paths.get(resource.getURI())));
      return template.replace(REPLACED_STRING, passwordChangeLink);
    } catch (IOException e) {
      throw new RuntimeException("Failed to read email template", e);
    }
  }

  private MimeMessage getPasswordChangeLink(String to, String passwordChangeLink)
      throws MessagingException {
    MimeMessage mimeMessage = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
    helper.setText(generateHtmlContent(passwordChangeLink), true); // Use this or above line.
    helper.setTo(to);
    helper.setSubject(SUBJECT);
    helper.setFrom(from);
    return mimeMessage;
  }

  private void saveAuthCode(String authCode, String email) {
    redisTemplate.opsForValue().set(
        authCode,
        email,
        5L,
        TimeUnit.MINUTES
    );
  }

  private String getAuthCode(String email) {
    String authCode = passwordEncoder.encode(email);
    saveAuthCode(authCode, email);
    return authCode;
  }

  private String getPasswordChangeLink(String email) {
    return "%s/user/passwordchange/%s".formatted(SERVER_ORIGIN, getAuthCode(email));
  }

  public void passwordChangeMail(String email) {
    try {
      mailSender.send(getPasswordChangeLink(email, getPasswordChangeLink(email)));
    } catch (MessagingException e) {
      throw new RuntimeException("Failed to send email", e);
    }
  }
}
