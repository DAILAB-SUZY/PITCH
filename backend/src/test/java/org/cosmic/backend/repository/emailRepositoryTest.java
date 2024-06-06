package org.cosmic.backend.repository;

import org.assertj.core.api.Assertions;
import org.cosmic.backend.domain.user.domain.Email;
import org.cosmic.backend.domain.user.domain.User;
import org.cosmic.backend.domain.user.repository.EmailRepository;
import org.cosmic.backend.domain.user.repository.UsersRepository;
import org.cosmic.backend.testcontainer.RepositoryBaseTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.testcontainers.junit.jupiter.Testcontainers;

@DataJpaTest
@Testcontainers
public class emailRepositoryTest extends RepositoryBaseTest {

    @Autowired
    private EmailRepository emailRepository;
    @Autowired
    private UsersRepository usersRepository;
    private User user;
    private Email email;
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
    @DisplayName("이메일 코드 생성")
    void saveEmail() {
        Email savedEmail=emailRepository.save(email);

        Assertions.assertThat(email.getEmail()).isEqualTo(savedEmail.getEmail());//이메일이 일치하게 들어가야함
        Assertions.assertThat(savedEmail.getVerified()).isNotNull();//무조건 false가 있어야함
        Assertions.assertThat(email.getVerificationCode()).isEqualTo(savedEmail.getVerificationCode());//인증코드가 일치하게 들어가야함
        Assertions.assertThat(savedEmail.getCreateTime()).isNotNull();//무조건 생성 시간이 존재해야함
        Assertions.assertThat(emailRepository.count()).isEqualTo(1);//한개를 저장했으니 한개만 있어야함.
    }

}
