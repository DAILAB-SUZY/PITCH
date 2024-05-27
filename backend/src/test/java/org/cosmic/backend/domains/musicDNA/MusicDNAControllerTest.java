package org.cosmic.backend.domains.musicDNA;

import lombok.extern.log4j.Log4j2;
import org.cosmic.backend.domain.auth.dto.UserLogin;
import org.cosmic.backend.domain.musicDNA.repository.DnaRepository;
import org.cosmic.backend.domain.user.domain.Email;
import org.cosmic.backend.domain.user.domain.User;
import org.cosmic.backend.domain.user.repository.EmailRepository;
import org.cosmic.backend.domain.user.repository.UsersRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Log4j2
public class MusicDNAControllerTest {

    @Autowired
    private DnaRepository dnaRepository;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private EmailRepository emailRepository;
    @Autowired
    private MockMvc mockMvc;

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        Email email = emailRepository.save(Email.builder()
            .email("testman@example.com")
            .verificationCode("123456")
            .verified(true)
            .build());

        usersRepository.save(User.builder()
            .email(email)
            .username("junho")
            .password(encoder.encode("123456"))
            .build());
    }

    @AfterEach
    public void tearDown() {
        emailRepository.deleteAll();
        usersRepository.deleteAll();
    }

    //DNA데이터 잘 주는지
    @Test
    public void request_DNA() throws Exception {
        List<String> TestDNA= Arrays.asList("슬픔","느슨한","감정적인","행복한");
        String json=mapper.writeValueAsString(TestDNA);
        System.out.println(json);
        mockMvc.perform(RestDocumentationRequestBuilders.post("/musicDNA")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk());
    }

    //DNA데이터 잘 받아서 들어가서 repository에 잘 들어갔는지
    @Test
    public void response_DNA() throws Exception {
        List<String>DNA=new ArrayList<String>();
        //dnaRepository
    }
}

