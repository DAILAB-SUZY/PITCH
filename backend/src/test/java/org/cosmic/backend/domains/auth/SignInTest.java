package org.cosmic.backend.domains.auth;

import lombok.extern.log4j.Log4j2;
import org.cosmic.backend.domain.auth.dto.UserLogin;
import org.cosmic.backend.domain.user.domain.Email;
import org.cosmic.backend.domain.user.domain.User;
import org.cosmic.backend.domain.user.repository.EmailRepository;
import org.cosmic.backend.domain.user.repository.UsersRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@AutoConfigureMockMvc
@Log4j2
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SignInTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private EmailRepository emailRepository;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    ObjectMapper mapper = new ObjectMapper();

    @Test
    @Order(1)
    public void signInTest() throws Exception {
        Email email = emailRepository.save(Email.builder()
                .email("testman@example.com")
                .verificationCode("123456")
                .verified(true)
                .build());

        usersRepository.save(User.builder()
                .email(email)
                .username("goodwill")
                .password(encoder.encode("123456"))
                .build());
        mockMvc.perform(post("/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(UserLogin.builder()
                                .email("testman@example.com")
                                .password("123456")
                                .build()
                        ))
                ).andDo(print())
                .andExpect(status().isOk());
        log.info(redisTemplate.opsForValue().get("testman@example.com"));
        Assertions.assertFalse(redisTemplate.opsForValue().get("testman@example.com").isEmpty());
    }

    @Test
    @Order(2)
    public void notMatchTest() throws Exception {
        mockMvc.perform(post("/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(UserLogin.builder()
                                .email("testman@example.com")
                                .password("123457")
                                .build()
                        ))
                ).andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void notMatchEmailTest() throws Exception {
        mockMvc.perform(post("/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(UserLogin.builder()
                                .email("tesswoman@example.com")
                                .password("123456")
                                .build()
                        ))
                ).andDo(print())
                .andExpect(status().isUnauthorized());
    }
}
