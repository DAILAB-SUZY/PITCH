package org.cosmic.backend.repository;
import org.assertj.core.api.Assertions;
import org.cosmic.backend.domain.user.domains.Email;
import org.cosmic.backend.domain.user.domains.User;
import org.cosmic.backend.domain.user.repositorys.EmailRepository;
import org.cosmic.backend.domain.user.repositorys.UsersRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
@Testcontainers
public class userRepositoryTest {

    @Autowired
    private UsersRepository usersRepository;
    private User user;
    private Email email;
    @Autowired
    private EmailRepository emailRepository;

    /*@BeforeEach
    public void setUp(){
        email=new Email();
        email.setEmail("kimjunho1231@naver.com");
        email.setVerificationCode("123456");
        emailRepository.save(email);
        user=new User();
        user.setEmail(email);
        user.setUsername("junho");
        user.setPassword("1234");
    }
    @AfterEach
    public void clean(){
        System.out.println("*******2");
        usersRepository.deleteAll();
        System.out.println("*******3");
        emailRepository.deleteAll();
        System.out.println("*******4");
    }*/

    @Test
    @DisplayName("유저 저장 확인")
    public void saveUserTest() {

        email=new Email();
        email.setEmail("kimjunho1231@naver.com");
        email.setVerificationCode("123456");
        emailRepository.save(email);

        user=new User();
        user.setEmail(email);
        user.setUsername("junho");
        user.setPassword("1234");

        // given
        // when
        User savedUser=usersRepository.save(user);

        // then

        Assertions.assertThat(savedUser.getEmail()).isNotNull();//NOT NULL확인
        Assertions.assertThat(user.getEmail()).isEqualTo(savedUser.getEmail());//이메일이 일치하게 들어가야함//fk도 확인

        Assertions.assertThat(savedUser.getPassword()).isNotNull();//NOT NULL확인
        Assertions.assertThat(user.getPassword()).isEqualTo(savedUser.getPassword());//인증코드가 일치하게 들어가야함

        Assertions.assertThat(user.getProfilePicture()).isEqualTo(savedUser.getProfilePicture());//인증코드가 일치하게 들어가야함

        Assertions.assertThat(user.getSignupDate()).isEqualTo(savedUser.getSignupDate());//인증코드가 일치하게 들어가야함

        System.out.println("*******1");
    }

    @Test
    @DisplayName("전체 유저 목록 조회")
    public void findUserListTest() {
        email=new Email();
        email.setEmail("kimjunho1232@naver.com");
        email.setVerificationCode("123456");
        email.setVerified(true);
        emailRepository.save(email);

        System.out.println("******1");
        Email email1=new Email();
        email1.setEmail("kimjunho1232@google.co.kr");
        email1.setVerificationCode("123456");
        email1.setVerified(true);
        emailRepository.save(email1);
        System.out.println("******2");

        user=new User();
        user.setEmail(email);
        user.setUsername("junho");
        user.setPassword("1234");

        System.out.println("******3");

        User user2=new User();
        user2.setEmail(email1);
        user2.setUsername("junho");
        user2.setPassword("123");

        System.out.println("******4");

        usersRepository.save(user);
        System.out.println("******5");
        usersRepository.save(user2);
        System.out.println("******6");

        //when
        List<User> findList= usersRepository.findAll();
    }

    @Test
    @DisplayName("유저 이메일로 조회")
    public void findUserByEmailTest(){

        email=new Email();
        email.setEmail("kimjunho12313@naver.com");
        email.setVerificationCode("123456");
        emailRepository.save(email);
        user=new User();
        user.setEmail(email);
        user.setUsername("junho");
        user.setPassword("1234");
        // given
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
    public void findUserLoginTest(){

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
