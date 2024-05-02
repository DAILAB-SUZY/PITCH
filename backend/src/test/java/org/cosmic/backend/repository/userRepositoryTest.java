package org.cosmic.backend.repository;

import org.assertj.core.api.Assertions;
import org.cosmic.backend.domain.user.domain.User;
import org.cosmic.backend.domain.user.repository.UsersRepository;
import org.cosmic.backend.testcontainer.RepositoryBaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
@Testcontainers
public class userRepositoryTest extends RepositoryBaseTest {

    @Autowired
    private UsersRepository usersRepository;
    private User user;

    @BeforeEach
    public void setUp(){
        user=new User();
        user.setEmail("kimjunho1231@naver.com");
        user.setUsername("junho");
        user.setPassword("123");
    }

    @Test
    @DisplayName("유저 저장 확인")
    public void saveUserTest() {
        // given
        // when
        User savedUser=usersRepository.save(user);

        // then
        Assertions.assertThat(user.getId()).isEqualTo(1);//id가 1인지

        Assertions.assertThat(savedUser.getEmail()).isNotNull();//NOT NULL확인
        Assertions.assertThat(user.getEmail()).isEqualTo(savedUser.getEmail());//이메일이 일치하게 들어가야함//fk도 확인

        Assertions.assertThat(savedUser.getPassword()).isNotNull();//NOT NULL확인
        Assertions.assertThat(user.getPassword()).isEqualTo(savedUser.getPassword());//인증코드가 일치하게 들어가야함

        Assertions.assertThat(user.getProfilePicture()).isEqualTo(savedUser.getProfilePicture());//인증코드가 일치하게 들어가야함

        Assertions.assertThat(user.getSignupDate()).isEqualTo(savedUser.getSignupDate());//인증코드가 일치하게 들어가야함
        Assertions.assertThat(usersRepository.count()).isEqualTo(1);//한개를 저장했으니 한개만 있어야함.
    }

    //
}
