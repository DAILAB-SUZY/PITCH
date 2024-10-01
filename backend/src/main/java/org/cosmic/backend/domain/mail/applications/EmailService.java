package org.cosmic.backend.domain.mail.applications;

import lombok.RequiredArgsConstructor;
import org.cosmic.backend.domain.mail.dtos.EmailAddress;
import org.cosmic.backend.domain.mail.exceptions.ExistEmailException;
import org.cosmic.backend.domain.mail.exceptions.IntervalNotEnoughException;
import org.cosmic.backend.domain.mail.utils.MailContentGenerator;
import org.cosmic.backend.domain.user.domains.Email;
import org.cosmic.backend.domain.user.exceptions.NotExistEmailException;
import org.cosmic.backend.domain.user.exceptions.NotMatchPasswordException;
import org.cosmic.backend.domain.user.repositorys.EmailRepository;
import org.cosmic.backend.domain.user.repositorys.UsersRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

/**
 * 이메일 관련 서비스를 제공하는 클래스입니다.
 * 인증 이메일 전송 및 인증 코드 검증 기능을 포함합니다.
 */
@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    private final MailContentGenerator mailContentGenerator = new MailContentGenerator();
    private final EmailRepository emailRepository;
    private final UsersRepository usersRepository;

    /**
     * 주어진 이메일 주소가 이미 존재하는지 확인합니다.
     *
     * @param to 확인할 이메일 주소
     * @throws ExistEmailException 이메일이 이미 존재하는 경우 발생합니다.
     */
    private void emailExistCheck(String to){
        if(usersRepository.findByEmail_Email(to).isPresent()){
            throw new ExistEmailException();
        }
    }

    /**
     * 이메일 요청 간격이 충분한지 확인합니다.
     *
     * @param start 이전 이메일 요청 시간
     * @param end 현재 이메일 요청 시간
     * @throws IntervalNotEnoughException 이메일 요청 간격이 너무 짧은 경우 발생합니다.
     */
    private void durationValidCheck(Instant start, Instant end){
        int WAIT_TIME = 30;
        if(Math.abs(Duration.between(start, end).toSeconds()) < WAIT_TIME){
            throw new IntervalNotEnoughException();
        }
    }

    /**
     * 주어진 이메일 주소로 인증 이메일을 전송합니다. (임의 코드)
     *
     * @param to 인증 이메일을 보낼 이메일 주소
     * @param content 이메일에 포함될 인증 코드
     * @throws ExistEmailException 이메일이 이미 존재하는 경우 발생합니다.
     * @throws IntervalNotEnoughException 이메일 요청 간격이 너무 짧은 경우 발생합니다.
     * @throws RuntimeException 예기치 못한 오류가 발생한 경우 발생합니다.
     */
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

    /**
     * 주어진 이메일 주소로 인증 이메일을 전송합니다. (랜덤 코드)
     *
     * @param to 인증 이메일을 보낼 이메일 주소
     */
    public void sendVerificationEmail(String to){
        mailSender.send(mailContentGenerator.verificationMessage(to));
    }

    /**
     * 주어진 이메일 주소와 인증 코드를 통해 이메일을 인증합니다.
     *
     * @param email 인증할 이메일 주소
     * @param code 사용자가 입력한 인증 코드
     * @return 인증된 이메일 주소를 포함한 {@link ResponseEntity}
     * @throws NotExistEmailException 이메일이 존재하지 않는 경우 발생합니다.
     * @throws NotMatchPasswordException 인증 코드가 일치하지 않는 경우 발생합니다.
     */
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
