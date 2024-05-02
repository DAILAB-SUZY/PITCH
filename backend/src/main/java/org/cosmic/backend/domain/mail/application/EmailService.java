package org.cosmic.backend.domain.mail.application;

import lombok.RequiredArgsConstructor;
import org.cosmic.backend.domain.mail.exceptions.ExistEmailException;
import org.cosmic.backend.domain.mail.utils.MailContentGenerator;
import org.cosmic.backend.domain.user.domain.Email;
import org.cosmic.backend.domain.user.domain.User;
import org.cosmic.backend.domain.user.repository.EmailRepository;
import org.cosmic.backend.domain.user.repository.UsersRepository;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    private final MailContentGenerator mailContentGenerator = new MailContentGenerator();
    private final EmailRepository emailRepository;
    private final UsersRepository usersRepository;

    public void sendVerificationEmail(String to, String content) {
        if(!emailRepository.existsById(to)){
            emailRepository.save(Email.builder().email(to).verificationCode(content).build());
        }
        Optional<User> user = usersRepository.findByEmail(to);
        if(user.isPresent()){
            throw new ExistEmailException();
        }
        mailSender.send(mailContentGenerator.verificationMessage(to, content));
    }

    public void sendVerificationEmail(String to){
        mailSender.send(mailContentGenerator.verificationMessage(to));
    }
}
