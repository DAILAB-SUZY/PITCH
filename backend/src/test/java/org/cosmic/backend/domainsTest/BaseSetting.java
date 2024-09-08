package org.cosmic.backend.domainsTest;

import lombok.extern.log4j.Log4j2;
import org.cosmic.backend.domain.auth.dtos.UserLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.time.Instant;

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
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(userLogin)));
        MvcResult result = resultActions.andReturn();
        String validToken = mapper.readValue(result.getResponse().getContentAsString(), UserLogin.class).getToken();
        return UserLogin.builder()
                .email(email)
                .token(validToken)
                .build();
    }

    protected <T> ResultActions mockMvcHelper(String url, Long id,T requestObject,String validToken) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post(url,id)
                .header("Authorization", "Bearer " + validToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(requestObject)));
    }
    protected ResultActions mockMvcsHelper(String url, Long id1,Long id2,String validToken) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post(url,id1,id2)
                .header("Authorization", "Bearer " + validToken)
                .contentType(MediaType.APPLICATION_JSON));
    }
    protected <T> ResultActions mockMvcHelper(String url, Long id1,Long id2,T requestObject,String validToken) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post(url,id1,id2)
                .header("Authorization", "Bearer " + validToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(requestObject)));
    }
    protected ResultActions mockMvcHelper(String url, Long id,String validToken) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post(url,id)
                .header("Authorization", "Bearer " + validToken)
                .contentType(MediaType.APPLICATION_JSON)
        );
    }
    protected <T> ResultActions mockMvcHelper(String url, T requestObject,String validToken) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post(url)
                .header("Authorization", "Bearer " + validToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(requestObject)));
    }


    protected <T>  ResultActions mockMvcGetHelper(String url, Long requestId,T requestObject,String validToken) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.get(url,requestId)
                .header("Authorization", "Bearer " +validToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(requestObject))
        );
    }
    protected  ResultActions mockMvcGetsHelper(String url, Long requestId,Long requestId2,String validToken) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.get(url,requestId,requestId2)
                .header("Authorization", "Bearer " +validToken)
                .contentType(MediaType.APPLICATION_JSON)
        );
    }
    protected  ResultActions mockMvcGetHelper(String url, String validToken) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.get(url)
                .header("Authorization", "Bearer " +validToken)
                .contentType(MediaType.APPLICATION_JSON)
        );
    }
    protected <T>  ResultActions mockMvcGetHelper(String url, String request,T requestObject,String validToken) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.get(url,request)
                .header("Authorization", "Bearer " +validToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(requestObject))
        );
    }
    protected <T> ResultActions mockMvcGetHelper(String url, T request,String validToken) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.get(url,request)
                .header("Authorization", "Bearer " +validToken)
                .contentType(MediaType.APPLICATION_JSON)
        );
    }

    protected <T>  ResultActions mockMvcDeleteHelper(String url, Long requestId,T requestObject,String validToken) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.delete(url,requestId)
                .header("Authorization", "Bearer " +validToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(requestObject))
        );
    }

    protected  ResultActions mockMvcDeletesHelper(String url, Long requestId1,Long requestId2,String validToken) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.delete(url,requestId1,requestId2)
                .header("Authorization", "Bearer " +validToken)
                .contentType(MediaType.APPLICATION_JSON)
        );
    }

    protected <T> ResultActions mockMvcDeleteHelper(String url, T request,String validToken) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.delete(url,request)
                .header("Authorization", "Bearer " +validToken)
                .contentType(MediaType.APPLICATION_JSON)
        );
    }

}
