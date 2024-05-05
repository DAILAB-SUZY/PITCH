package org.cosmic.backend.domain.mail.application;

import lombok.RequiredArgsConstructor;
import org.cosmic.backend.domain.mail.exceptions.ExistEmailException;
import org.cosmic.backend.domain.mail.exceptions.IntervalNotEnoughException;
import org.cosmic.backend.domain.mail.utils.MailContentGenerator;
import org.cosmic.backend.domain.user.domain.Email;
import org.cosmic.backend.domain.user.repository.EmailRepository;
import org.cosmic.backend.domain.user.repository.UsersRepository;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    private final MailContentGenerator mailContentGenerator = new MailContentGenerator();
    private final EmailRepository emailRepository;
    private final UsersRepository usersRepository;

    private final int WAIT_TIME = 30;

    private void emailExistCheck(String to){
        if(usersRepository.findByEmail(to).isPresent()){
            throw new ExistEmailException();
        }
    }

    private void durationValidCheck(Instant start, Instant end){
        if(Math.abs(Duration.between(start, end).toSeconds()) < WAIT_TIME){
            throw new IntervalNotEnoughException();
        }
    }

    public void sendVerificationEmail(String to, String content) {
        emailRepository.findById(to).ifPresentOrElse(
                email -> {
                    emailExistCheck(email.getEmail());
                    durationValidCheck(email.getCreateTime(), Instant.now());
                },
                () -> emailRepository.save(Email.builder().email(to).verificationCode(content).build())
        );
        emailRepository.findById(to).ifPresentOrElse(
                email -> {
                    email.setVerificationCode(content);
                    email.setCreateTime(Instant.now());
                    email.setVerified(false);
                    emailRepository.save(email);
                },
                () -> {throw new RuntimeException();}
        );
        mailSender.send(mailContentGenerator.verificationMessage(to, content));
    }

    public void sendVerificationEmail(String to){
        mailSender.send(mailContentGenerator.verificationMessage(to));
    }
}
