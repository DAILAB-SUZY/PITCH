package org.cosmic.backend.domains.auth;

import org.cosmic.backend.domain.auth.dto.UserLogin;
import org.cosmic.backend.domain.user.domain.Email;
import org.cosmic.backend.domain.user.domain.User;
import org.cosmic.backend.domain.user.repository.EmailRepository;
import org.cosmic.backend.domain.user.repository.UsersRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RefreshTokenTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private EmailRepository emailRepository;
    @Autowired
    private UsersRepository usersRepository;

    ObjectMapper mapper = new ObjectMapper();
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    @Qualifier("redisTemplate")
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Test
    @Order(1)
    public void reIssuedTest() throws Exception {
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
                )
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(redisTemplate.opsForValue().get(email.getEmail()))));

        String refreshToken = redisTemplate.opsForValue().get(email.getEmail());

        mockMvc.perform(post("/auth/reissued")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Refresh-Token", refreshToken)
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(redisTemplate.opsForValue().get(email.getEmail()))));
    }

    @Test
    @Order(2)
    public void invalidRefreshTokenTest() throws Exception {
        mockMvc.perform(post("/auth/reissued")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Refresh-Token", "1231231231231232")
        ).andDo(print())
                .andExpect(status().isUnauthorized());
    }
}
