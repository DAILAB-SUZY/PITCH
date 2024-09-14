package org.cosmic.backend.domainsTest.musicDNA;

import lombok.extern.log4j.Log4j2;
import org.cosmic.backend.domain.auth.dtos.UserLoginDetail;
import org.cosmic.backend.domain.musicDna.dtos.DnaDetail;
import org.cosmic.backend.domain.musicDna.dtos.DnaDto;
import org.cosmic.backend.domain.musicDna.repositorys.MusicDnaRepository;
import org.cosmic.backend.domain.user.domains.User;
import org.cosmic.backend.domain.user.repositorys.UsersRepository;
import org.cosmic.backend.domainsTest.BaseSetting;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.shaded.com.fasterxml.jackson.core.type.TypeReference;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Log4j2
public class MusicDNAControllerTest extends BaseSetting {
    @Autowired
    private UsersRepository userRepository;
    @Autowired
    private MockMvc mockMvc;

    final ObjectMapper mapper = new ObjectMapper();
    @Autowired
    private MusicDnaRepository musicDnaRepository;
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    //DNA데이터 잘 주는지
    @Test
    @Transactional
    @Sql("/data/musicDna.sql")
    public void dnaRequestTest() throws Exception {
        User user=userRepository.findByEmail_Email("test1@example.com").get();
        user.setPassword(encoder.encode(user.getPassword()));
        UserLoginDetail userLogin = loginUser("test1@example.com");
        ResultActions resultActions=mockMvcHelper(HttpMethod.GET,"/api/dna",null,userLogin.getToken())
        .andExpect(status().isOk());
        String responseBody=resultActions.andReturn().getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        List<DnaDetail>dnaList=mapper.readValue(responseBody,new TypeReference<List<DnaDetail>>() {});
        assertEquals("lazy", dnaList.get(0).getDnaName());
        assertEquals("funny", dnaList.get(1).getDnaName());
    }

    @Test
    @Transactional
    @Sql("/data/musicDna.sql")
    public void dnaSaveTest() throws Exception {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        User user=userRepository.findByEmail_Email("test1@example.com").get();
        user.setPassword(encoder.encode(user.getPassword()));
        UserLoginDetail userLogin = loginUser("test1@example.com");
        DnaDto dnaDTO=new DnaDto();
        dnaDTO.setDna(Arrays.asList(1L,2L,3L,4L));

        mockMvcHelper(HttpMethod.POST,"/api/dna",dnaDTO,userLogin.getToken()).andExpect(status().isOk());
    }

    @Test
    @Transactional
    @Sql("/data/musicDna.sql")
    public void notMatchDataTest() throws Exception {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        User user=userRepository.findByEmail_Email("test1@example.com").get();
        user.setPassword(encoder.encode(user.getPassword()));
        UserLoginDetail userLogin = loginUser("test1@example.com");
        DnaDto dnaDTO=new DnaDto();
        dnaDTO.setDna(Arrays.asList(1L,2L,3L));
        mockMvcHelper(HttpMethod.POST,"/api/dna",dnaDTO,userLogin.getToken()).andExpect(status().isBadRequest());
    }
}

