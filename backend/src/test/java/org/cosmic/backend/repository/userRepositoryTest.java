package org.cosmic.backend.repository;
import org.assertj.core.api.Assertions;
import org.cosmic.backend.domain.user.domains.Email;
import org.cosmic.backend.domain.user.domains.User;
import org.cosmic.backend.domain.user.repositorys.EmailRepository;
import org.cosmic.backend.domain.user.repositorys.UsersRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

@SpringBootTest
@Testcontainers
public class userRepositoryTest {

    @Autowired
    private UsersRepository usersRepository;
    private User user;
    private Email email;
    @Autowired
    private EmailRepository emailRepository;

    @Test
    @DisplayName("유저 저장 확인")
    public void userSaveTest() {

        email=new Email();
        email.setEmail("kimjunho1231@naver.com");
        email.setVerificationCode("123456");
        emailRepository.save(email);

        user=new User();
        user.setEmail(email);
        user.setUsername("junho");
        user.setPassword("1234");

        User savedUser=usersRepository.save(user);


        Assertions.assertThat(savedUser.getEmail()).isNotNull();//NOT NULL확인
        Assertions.assertThat(user.getEmail()).isEqualTo(savedUser.getEmail());

        Assertions.assertThat(savedUser.getPassword()).isNotNull();//NOT NULL확인
        Assertions.assertThat(user.getPassword()).isEqualTo(savedUser.getPassword());

        Assertions.assertThat(user.getProfilePicture()).isEqualTo(savedUser.getProfilePicture());

        Assertions.assertThat(user.getSignupDate()).isEqualTo(savedUser.getSignupDate());

    }

    @Test
    @DisplayName("전체 유저 목록 조회")
    public void userListFindTest() {
        email=new Email();
        email.setEmail("kimjunho1232@naver.com");
        email.setVerificationCode("123456");
        email.setVerified(true);
        emailRepository.save(email);

        Email email1=new Email();
        email1.setEmail("kimjunho1232@google.co.kr");
        email1.setVerificationCode("123456");
        email1.setVerified(true);
        emailRepository.save(email1);

        user=new User();
        user.setEmail(email);
        user.setUsername("junho");
        user.setPassword("1234");

        User user2=new User();
        user2.setEmail(email1);
        user2.setUsername("junho");
        user2.setPassword("123");

        usersRepository.save(user);
        usersRepository.save(user2);

        List<User> findList= usersRepository.findAll();
    }

    @Test
    @DisplayName("유저 이메일로 조회")
    public void userByEmailFindTest(){

        email=new Email();
        email.setEmail("kimjunho12313@naver.com");
        email.setVerificationCode("123456");
        emailRepository.save(email);
        user=new User();
        user.setEmail(email);
        user.setUsername("junho");
        user.setPassword("1234");
        Email email1=new Email();
        email1.setEmail("kimjunho12313@google.co.kr");
        email1.setVerificationCode("123456");
        emailRepository.save(email1);

        User user2=new User();
        user2.setEmail(email1);
        user2.setUsername("junho");
        user2.setPassword("123");
        usersRepository.save(user);
        usersRepository.save(user2);

        // when
        User searchuser=usersRepository.findByEmail_Email("kimjunho12313@naver.com").get();

        // then
        Assertions.assertThat(searchuser.getUserId()).isGreaterThan(0);
    }

    @Test
    @DisplayName("유저 로그인 확인")
    public void userLoginFindTest(){

        email=new Email();
        email.setEmail("kimjunho12314@naver.com");
        email.setVerificationCode("123456");
        emailRepository.save(email);

        Email email1=new Email();
        email1.setEmail("kimjunho12314@google.co.kr");
        email1.setVerificationCode("123456");
        emailRepository.save(email1);

        user=new User();
        user.setEmail(email);
        user.setUsername("junho");
        user.setPassword("1234");

        User user2=new User();
        user2.setEmail(email1);
        user2.setUsername("junho");
        user2.setPassword("123");
        usersRepository.save(user);
        usersRepository.save(user2);

        User searchuser=usersRepository.findByEmail_Email("kimjunho12314@naver.com").get();//아이디로 확인 후
        if(searchuser.getPassword().equals("1234")){//입력받은 비번이랑 일치하다면
            System.out.println("ok");
            Assertions.assertThat(searchuser.getUserId()).isGreaterThan(0);
            Assertions.assertThat(searchuser.getUsername().equals("junho"));
        }
    }
}
