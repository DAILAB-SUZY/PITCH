package org.cosmic.backend.repository;
import org.assertj.core.api.Assertions;
import org.cosmic.backend.domain.user.domain.Email;
import org.cosmic.backend.domain.user.domain.User;
import org.cosmic.backend.domain.user.repository.EmailRepository;
import org.cosmic.backend.domain.user.repository.UsersRepository;
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

    @BeforeEach
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
        usersRepository.deleteAll();
        emailRepository.deleteAll();
    }

    @Test
    @DisplayName("유저 저장 확인")
    public void saveUserTest() {
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
        Assertions.assertThat(usersRepository.count()).isEqualTo(1);//한개를 저장했으니 한개만 있어야함.
    }

    @Test
    @DisplayName("전체 유저 목록 조회")
    public void findUserListTest() {
        //given
        Email email1=new Email();
        email1.setEmail("kimjunho1231@google.co.kr");
        email1.setVerificationCode("123456");
        emailRepository.save(email1);

        User user2=new User();
        user2.setEmail(email1);
        user2.setUsername("junho");
        user2.setPassword("123");
        usersRepository.save(user);
        usersRepository.save(user2);

        //when
        List<User> findList= usersRepository.findAll();

        //then
        for(User user:findList){
            assertThat(user.getId()).isNotNull();
            assertThat(user.getEmail()).isNotNull();
            assertThat(user.getUsername()).isNotNull();
            assertThat(user.getPassword()).isNotNull();
            assertThat(user.getProfilePicture()).isNotNull();
            assertThat(user.getSignupDate()).isNotNull();
        }
    }

    @Test
    @DisplayName("유저 이메일로 조회")
    public void findUserByEmailTest(){

        // given
        Email email1=new Email();
        email1.setEmail("kimjunho1231@google.co.kr");
        email1.setVerificationCode("123456");
        emailRepository.save(email1);

        User user2=new User();
        user2.setEmail(email1);
        user2.setUsername("junho");
        user2.setPassword("123");
        usersRepository.save(user);
        usersRepository.save(user2);

        // when
        User searchuser=usersRepository.findByEmail_Email("kimjunho1231@naver.com").get();

        // then
        Assertions.assertThat(searchuser.getId()).isGreaterThan(0);
    }

    @Test
    @DisplayName("유저 로그인 확인")
    public void findUserLoginTest(){

        Email email1=new Email();
        email1.setEmail("kimjunho1231@google.co.kr");
        email1.setVerificationCode("123456");
        emailRepository.save(email1);

        User user2=new User();
        user2.setEmail(email1);
        user2.setUsername("junho");
        user2.setPassword("123");
        usersRepository.save(user);
        usersRepository.save(user2);

        User searchuser=usersRepository.findByEmail_Email("kimjunho1231@naver.com").get();//아이디로 확인 후
        if(searchuser.getPassword().equals("1234")){//입력받은 비번이랑 일치하다면
            System.out.println("ok");
            Assertions.assertThat(searchuser.getId()).isGreaterThan(0);
            Assertions.assertThat(searchuser.getUsername().equals("junho"));
        }
    }
}
