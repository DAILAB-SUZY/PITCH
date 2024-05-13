package org.cosmic.backend.domain.mail.application;

import lombok.RequiredArgsConstructor;
import org.cosmic.backend.domain.mail.dto.EmailAddress;
import org.cosmic.backend.domain.mail.exceptions.ExistEmailException;
import org.cosmic.backend.domain.mail.exceptions.IntervalNotEnoughException;
import org.cosmic.backend.domain.mail.utils.MailContentGenerator;
import org.cosmic.backend.domain.user.domain.Email;
import org.cosmic.backend.domain.user.exceptions.NotExistEmailException;
import org.cosmic.backend.domain.user.exceptions.NotMatchPasswordException;
import org.cosmic.backend.domain.user.repository.EmailRepository;
import org.cosmic.backend.domain.user.repository.UsersRepository;
import org.springframework.http.ResponseEntity;
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
        if(usersRepository.findByEmail_Email(to).isPresent()){
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
                    //plus 부분에 사용자 시간대로 수정해야 함.
                },
                () -> emailRepository.saveAndFlush(Email.builder().email(to).verificationCode(content).build())
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

    public ResponseEntity<EmailAddress> verifyCode(String email, String code) {
        Email user = emailRepository.findById(email).orElseThrow(NotExistEmailException::new);

        if(!code.equals(user.getVerificationCode())){
            throw new NotMatchPasswordException();
        }
        user.setVerified(true);
        emailRepository.save(user);
        return ResponseEntity.ok(EmailAddress.builder().email(email).build());
    }
}
