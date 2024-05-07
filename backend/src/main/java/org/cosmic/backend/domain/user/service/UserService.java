package org.cosmic.backend.domain.user.service;
import org.cosmic.backend.domain.user.domain.Email;
import org.cosmic.backend.domain.user.domain.User;
import org.cosmic.backend.domain.user.dto.JoinRequest;
import org.cosmic.backend.domain.user.repository.EmailRepository;
import org.cosmic.backend.domain.user.repository.UsersRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserService {

    private UsersRepository usersRepository;
    private EmailRepository emailRepository;

    public UserService(UsersRepository usersRepository,EmailRepository emailRepository) {
        this.usersRepository = usersRepository;
        this.emailRepository = emailRepository;
    }

    public void registerUser(JoinRequest request) throws Exception {

        //데이터 검증
        if (request.getEmail() == null || request.getPassword() == null || request.getCheckPassword() == null || request.getName() == null) {
            throw new IllegalArgumentException("빈 데이터가 있습니다.");
        }

        if (!request.getPassword().equals(request.getCheckPassword())) {
            throw new IllegalArgumentException("비밀번호와 확인비밀번호가 일치하지않습니다.");
        }

        // 이메일일치 여부 검증
        Optional<Email> emailOpt = emailRepository.findByEmail(request.getEmail());

        //이메일 인증 확인
        if (emailOpt.isEmpty() || !emailOpt.get().getVerified()) {//해당 이메일이 등록된게 아니라면
            throw new Exception("이메일이 등록되지 않았거나 없는 이메일입니다.");
        }

        /*비밀번호 조건 확인
        8자이상,
        */

        if(request.getPassword().length() < 8) {
            throw new Exception("비밀번호 형식에 맞지 않습니다..");
        }

        else{
            // 모든 조건 만족했으므로 유저 객체 생성 및 저장
            User newUser = new User();
            newUser.setEmail(emailOpt.get());
            //패스워드 인코더
            newUser.setPassword(request.getPassword());
            newUser.setUsername(request.getName());
            usersRepository.save(newUser);//유저 테이블 새로 생성

        }
    }
}
