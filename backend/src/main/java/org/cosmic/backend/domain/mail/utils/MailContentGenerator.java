package org.cosmic.backend.domain.mail.utils;

import org.springframework.mail.SimpleMailMessage;

public class MailContentGenerator {
    private static final RandomCodeGenerator randomCodeGenerator = new ApacheMathRandomCodeGenerator();

    public SimpleMailMessage verificationMessage(String to, String content){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setText(content);
        message.setSubject("Message from Java Mail Sender");
        return message;
    }

    public SimpleMailMessage verificationMessage(String to){
        return verificationMessage(to, randomCodeGenerator.randomCode());
    }
}
