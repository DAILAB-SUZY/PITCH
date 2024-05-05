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

    public void sendVerificationEmail(String to, String content) {
        emailRepository.findById(to).ifPresentOrElse(
                email -> {
                    if(usersRepository.findByEmail(to).isPresent()){
                        throw new ExistEmailException();
                    }
                    if(Math.abs(Duration.between(email.getCreateTime(), Instant.now()).toSeconds()) < 30){
                        throw new IntervalNotEnoughException();
                    }
                },
                () -> {
                    emailRepository.save(Email.builder().email(to).verificationCode(content).build());
                }
        );
        Email email = emailRepository.findById(to).orElseThrow(RuntimeException::new);
        email.setVerificationCode(content);
        email.setCreateTime(Instant.now());
        email.setVerified(false);
        emailRepository.save(email);
        mailSender.send(mailContentGenerator.verificationMessage(to, content));
    }

    public void sendVerificationEmail(String to){
        mailSender.send(mailContentGenerator.verificationMessage(to));
    }
}
