package org.cosmic.backend.domains.mail;

import lombok.extern.log4j.Log4j2;
import org.cosmic.backend.domain.mail.dto.VerificationForm;
import org.cosmic.backend.domain.user.domain.Email;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Log4j2
public class RandomCodeCheckTest extends EmailBaseTest{

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void emailExistCheck() {
        ObjectMapper objectMapper = new ObjectMapper();
        emailRepository.save(Email.builder().email("testman@example.com").verificationCode("123456").build());

        try {
            mockMvc.perform(post("/mail/verify")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(new VerificationForm("testman2@example.com", "123457"))))
                    .andDo(print())
                    .andExpect(status().isUnauthorized())
                    .andExpect(content().string(containsString("Not found email")));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void codeNotMatchCheck() {
        ObjectMapper objectMapper = new ObjectMapper();
        emailRepository.save(Email.builder().email("testman@example.com").verificationCode("123456").build());

        try {
            mockMvc.perform(post("/mail/verify")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(new VerificationForm("testman@example.com", "123457"))))
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(content().string(containsString("Not match password")));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
