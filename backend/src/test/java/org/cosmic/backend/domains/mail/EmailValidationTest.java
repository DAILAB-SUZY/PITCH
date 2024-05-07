package org.cosmic.backend.domains.mail;

import com.icegreen.greenmail.mail.MailAddress;
import lombok.extern.log4j.Log4j2;
import org.cosmic.backend.domain.user.domain.Email;
import org.cosmic.backend.domain.user.domain.User;
import org.cosmic.backend.domain.user.repository.UsersRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Log4j2
class EmailValidationTest extends EmailBaseTest {

    @Autowired
    private UsersRepository usersRepository;

    @Test
    public void alreadyExistEmailTest() {
        MailAddress mailAddress = new MailAddress("test1@example.com");
        ObjectMapper objectMapper = new ObjectMapper();
        Email email = emailRepository.save(Email.builder().email(mailAddress.getEmail()).verificationCode("123456").build());
        usersRepository.save(User.builder().email(email).username("testman").password("123456").profilePicture(null).build());

        try {
            this.mockMvc.perform(post("/mail/request")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(mailAddress)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString("Already exist email")));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void verifyEmailBeforeInterval() {
        MailAddress mailAddress = new MailAddress("twotimes@example.com");
        ObjectMapper objectMapper = new ObjectMapper();
        emailRepository.save(Email.builder().email(mailAddress.getEmail()).verificationCode("123456").createTime(Instant.now().plus(9, ChronoUnit.HOURS)).build());

        try {
            this.mockMvc.perform(post("/mail/request")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(mailAddress)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString("Not even 30 seconds passed")));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}
