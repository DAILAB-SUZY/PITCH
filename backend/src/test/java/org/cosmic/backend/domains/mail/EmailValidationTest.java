package org.cosmic.backend.domains.mail;

import com.icegreen.greenmail.mail.MailAddress;
import org.cosmic.backend.domain.user.domain.Email;
import org.cosmic.backend.domain.user.domain.User;
import org.cosmic.backend.domain.user.repository.UsersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class EmailValidationTest extends EmailBaseTest {

    @Autowired
    private UsersRepository usersRepository;

    @BeforeEach
    public void setUp(){
        final String[] emails = {"test1@example.com", "test2@example.com", "test3@example.com"};
        for(String email : emails){
            Email obj = new Email();
            obj.setEmail(email);
            obj.setVerificationCode("123456");
            User user = User.builder()
                    .email(email)
                    .password("123456")
                    .username("testman")
                    .profilePicture(null)
                    .build();
            usersRepository.save(user);
        }
    }

    @Test
    public void alreadyExistEmailTest() {
        MailAddress mailAddress = new MailAddress("test1@example.com");
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            this.mockMvc.perform(post("/mail/verify")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(mailAddress)))
                        .andDo(print())
                        .andExpect(status().isBadRequest())
                        .andExpect(content().string(containsString("Already exist email")));
        } catch (Exception e){
            fail(e.getMessage());
        }
    }
}
