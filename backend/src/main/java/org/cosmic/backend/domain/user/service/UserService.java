package org.cosmic.backend.domain.user.service;
import org.cosmic.backend.domain.auth.applications.TokenProvider;
import org.cosmic.backend.domain.auth.dto.UserLogin;
import org.cosmic.backend.domain.auth.exceptions.CredentialNotMatchException;
import org.cosmic.backend.domain.user.domain.Email;
import org.cosmic.backend.domain.user.domain.User;
import org.cosmic.backend.domain.user.dto.JoinRequest;
import org.cosmic.backend.domain.user.exceptions.NotExistEmailException;
import org.cosmic.backend.domain.user.exceptions.NotMatchConditionException;
import org.cosmic.backend.domain.user.exceptions.NotMatchPasswordException;
import org.cosmic.backend.domain.user.repository.EmailRepository;
import org.cosmic.backend.domain.user.repository.UsersRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final TokenProvider tokenProvider;
    private UsersRepository usersRepository;
    private EmailRepository emailRepository;

    public UserService(UsersRepository usersRepository, EmailRepository emailRepository, TokenProvider tokenProvider) {
        this.usersRepository = usersRepository;
        this.emailRepository = emailRepository;
        this.tokenProvider = tokenProvider;
    }

    public void registerUser(JoinRequest request){
        if (!request.getPassword().equals(request.getCheckPassword())) {
            throw new NotMatchPasswordException();
        }

        // 이메일일치 여부 검증
        Optional<Email> emailOpt = emailRepository.findByEmail(request.getEmail());

        //이메일 인증 확인
        if (emailOpt.isEmpty() || !emailOpt.get().getVerified()) {//해당 이메일이 등록된게 아니라면
            throw new NotExistEmailException();
        }

        /*비밀번호 조건 확인
        8자이상,
        */

        if(request.getPassword().length() < 8) {
            throw new NotMatchConditionException();
        }

        else{
            // 모든 조건 만족했으므로 유저 객체 생성 및 저장
            User newUser = new User();
            newUser.setEmail(emailOpt.get());
            BCryptPasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
            newUser.setPassword(passwordEncoder.encode(request.getPassword()));
            newUser.setUsername(request.getName());
            usersRepository.save(newUser);//유저 테이블 새로 생성

        }
    }

    public UserLogin getByCredentials(String email, String password) {
        BCryptPasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
        User user = usersRepository.findByEmail_Email(email).orElseThrow(CredentialNotMatchException::new);
        if(!passwordEncoder.matches(password, user.getPassword())) {
            throw new CredentialNotMatchException();
        }

        return UserLogin.builder()
                .token(tokenProvider.create(user))
                .email(email)
                .id(user.getId())
                .build();
    }
}