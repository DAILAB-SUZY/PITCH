package org.cosmic.backend.domain.mail.application;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    public void sendVerificationEmail(String to, String content){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("test.sender@hotmail.com");
        message.setSubject("Message from Java Mail Sender");
        message.setText(content);
        message.setTo(to);

        mailSender.send(message);
    }
}
