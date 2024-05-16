package org.cosmic.backend.domains.auth;

import lombok.extern.log4j.Log4j2;
import org.cosmic.backend.domain.auth.applications.ServletFilter;
import org.cosmic.backend.domain.auth.applications.TokenProvider;
import org.cosmic.backend.domain.user.repository.EmailRepository;
import org.cosmic.backend.domain.user.repository.UsersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@AutoConfigureMockMvc
@Log4j2
@WebMvcTest
@Import(ServletFilter.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ServletFilterTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @MockBean
    private TokenProvider tokenProvider; // TokenProvider의 모의 객체

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilter(new ServletFilter(), "/api/*") // 테스트할 필터와 적용할 URL 패턴 추가
                .build();
    }

    @Test
    public void shouldPassValidToken() throws Exception {
        String validToken = "validToken";
        when(tokenProvider.validateAndGetUserId(validToken)).thenReturn("userId123");

        mockMvc.perform(get("/api/test").header("Authorization", "Bearer " + validToken))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldRejectRequestWithoutToken() throws Exception {
        mockMvc.perform(get("/api/test"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void shouldRejectRequestWithNullToken() throws Exception {
        mockMvc.perform(get("/api/test").header("Authorization", "Bearer null"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void shouldHandleTokenProviderException() throws Exception {
        String invalidToken = "invalidToken";
        when(tokenProvider.validateAndGetUserId(invalidToken)).thenThrow(new RuntimeException("Invalid token"));

        mockMvc.perform(get("/api/test").header("Authorization", "Bearer " + invalidToken))
                .andExpect(status().isForbidden());
    }
}
