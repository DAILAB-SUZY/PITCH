package org.cosmic.backend.domains.auth;

import lombok.extern.log4j.Log4j2;
import org.cosmic.backend.domain.auth.applications.JwtAuthenticationFilter;
import org.cosmic.backend.domain.auth.dtos.UserLogin;
import org.cosmic.backend.domain.user.domains.Email;
import org.cosmic.backend.domain.user.domains.User;
import org.cosmic.backend.domain.user.repositorys.EmailRepository;
import org.cosmic.backend.domain.user.repositorys.UsersRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@AutoConfigureMockMvc
@Log4j2
@Import(JwtAuthenticationFilter.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ServletFilterTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private EmailRepository emailRepository;
    @Autowired
    private UsersRepository usersRepository;

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    ObjectMapper mapper = new ObjectMapper();
    private String validToken;
    //유효한 jwt토큰이 제공됐을 때 인증이 성공적으로 수행되는지 확인
    @Test
    public void Token_isValid() throws Exception {
        Email email = emailRepository.save(Email.builder()
            .email("testq1@example.com")
            .verificationCode("123456")
            .verified(true)
            .build());
        usersRepository.save(User.builder()
                .email(email)
                .username("goodwill")
                .password(encoder.encode("123456"))
                .build());
        UserLogin userLogin = UserLogin.builder()
                .email("testq1@example.com")
                .password("123456")
                .build();

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userLogin)))
                .andExpect(status().isOk());

        MvcResult result = resultActions.andReturn();
        validToken = mapper.readValue(result.getResponse().getContentAsString(), UserLogin.class).getToken();

        mockMvc.perform(MockMvcRequestBuilders.get("/example")
                        .header("Authorization", "Bearer " + validToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    //유효하지 않은 jwt토큰이 제공됐을 때 인증실패하는지 확인
    @Test
    public void Token_isnonValid() throws Exception {
        Email email = emailRepository.save(Email.builder()
                .email("testn1@example.com")
                .verificationCode("123456")
                .verified(true)
                .build());
        usersRepository.save(User.builder()
                .email(email)
                .username("goodwill")
                .password(encoder.encode("123456"))
                .build());
        UserLogin userLogin = UserLogin.builder()
                .email("testn1@example.com")
                .password("123456")
                .build();

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userLogin)))
                .andExpect(status().isOk());

        MvcResult result = resultActions.andReturn();
        validToken = mapper.readValue(result.getResponse().getContentAsString(), UserLogin.class).getToken();

        String invalidToken = "Bearerinvalid.token.jwt";

        mockMvc.perform(MockMvcRequestBuilders.get("/example")
                        .header("Authorization", "Bearer " + invalidToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}