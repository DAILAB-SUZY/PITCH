package org.cosmic.backend.domain.mail.application;

import lombok.RequiredArgsConstructor;
import org.cosmic.backend.domain.mail.utils.MailContentGenerator;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    private final MailContentGenerator mailContentGenerator = new MailContentGenerator();

    public void sendVerificationEmail(String to, String content){
        mailSender.send(mailContentGenerator.verificationMessage(to, content));
    }

    public void sendVerificationEmail(String to){
        mailSender.send(mailContentGenerator.verificationMessage(to));
    }
}
