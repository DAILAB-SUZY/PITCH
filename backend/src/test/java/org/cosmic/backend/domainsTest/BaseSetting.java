package org.cosmic.backend.domainsTest;

import lombok.extern.log4j.Log4j2;
import org.cosmic.backend.domain.auth.dtos.UserLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Log4j2
public class BaseSetting {
    @Autowired
    private MockMvc mockMvc;
    private final ObjectMapper mapper=new ObjectMapper();
    private ResultActions resultActions;

    protected UserLogin loginUser(String email) throws Exception {
        UserLogin userLogin = UserLogin.builder()
                .email(email)
                .password("12345678")
                .build();
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(userLogin)));
        MvcResult result = resultActions.andReturn();
        String validToken = mapper.readValue(result.getResponse().getContentAsString(), UserLogin.class).getToken();
        return UserLogin.builder()
                .email(email)
                .token(validToken)
                .build();
    }

    protected  <T> ResultActions mockMvcHelper(HttpMethod httpMethod, String url, T requestObject, String validToken) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.request(httpMethod, url)
                .header("Authorization", "Bearer " + validToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(requestObject)));
    }
}
