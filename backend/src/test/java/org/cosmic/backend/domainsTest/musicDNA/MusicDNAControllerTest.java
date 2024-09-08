package org.cosmic.backend.domainsTest.musicDNA;

import lombok.extern.log4j.Log4j2;
import org.cosmic.backend.domain.auth.dtos.UserLogin;
import org.cosmic.backend.domain.musicDna.domains.MusicDna;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    //DNA데이터 잘 주는지
    @Test
    @Transactional
    @Sql("/data/musicDna.sql")
    public void dnaRequestTest() throws Exception {
        mockMvc.perform(get("/api/dna")
        .contentType("application/json"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].emotion").value("lazy"))
        .andExpect(jsonPath("$[1].emotion").value("funny"));
    }

    @Test
    @Transactional
    @Sql("/data/musicDna.sql")
    public void dnaSaveTest() throws Exception {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        User user=userRepository.findByEmail_Email("test1@example.com").get();
        user.setPassword(encoder.encode(user.getPassword()));
        UserLogin userLogin = loginUser("test1@example.com");
        DnaDto dnaDTO=new DnaDto();
        dnaDTO.setDna(Arrays.asList(new DnaDetail(1L),new DnaDetail(2L),
            new DnaDetail(3L),new DnaDetail(4L)));
        mockMvcHelper("/api/dna",dnaDTO,userLogin.getToken()).andExpect(status().isOk());
    }

    @Test
    @Transactional
    @Sql("/data/musicDna.sql")
    public void notMatchDataTest() throws Exception {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        User user=userRepository.findByEmail_Email("test1@example.com").get();
        user.setPassword(encoder.encode(user.getPassword()));
        UserLogin userLogin = loginUser("test1@example.com");
        DnaDto dnaDTO=new DnaDto();
        dnaDTO.setDna(Arrays.asList(new DnaDetail(1L),new DnaDetail(2L),
                new DnaDetail(3L)));
        mockMvcHelper("/api/dna",dnaDTO,userLogin.getToken()).andExpect(status().isBadRequest());
    }
}

