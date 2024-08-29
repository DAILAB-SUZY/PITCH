package org.cosmic.backend.domainsTest.musicDNA;

import lombok.extern.log4j.Log4j2;
import org.cosmic.backend.domain.musicDna.domains.MusicDna;
import org.cosmic.backend.domain.musicDna.dtos.DnaDetail;
import org.cosmic.backend.domain.musicDna.dtos.DnaDto;
import org.cosmic.backend.domain.musicDna.repositorys.EmotionRepository;
import org.cosmic.backend.domain.user.domains.User;
import org.cosmic.backend.domain.user.repositorys.UsersRepository;
import org.cosmic.backend.domainsTest.BaseSetting;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Log4j2
public class MusicDNAControllerTest extends BaseSetting {
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private MockMvc mockMvc;

    final ObjectMapper mapper = new ObjectMapper();
    @Autowired
    private EmotionRepository emotionRepository;

    //DNA데이터 잘 주는지
    @Test
    @Transactional
    @Sql("/data/musicDna.sql")
    public void dnaRequestTest() throws Exception {
        List<MusicDna> DNA= emotionRepository.findAll();
        mockMvc.perform(post("/api/dna/give")
        .contentType("application/json")
        .content(mapper.writeValueAsString(DNA)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].emotion").value("lazy"))
        .andExpect(jsonPath("$[1].emotion").value("funny"));
    }

    @Test
    @Transactional
    @Sql("/data/musicDna.sql")
    public void dnaSaveTest() throws Exception {
        User user=usersRepository.findByEmail_Email("test1@example.com").get();
        DnaDto dnaDTO=new DnaDto(user.getUserId());
        dnaDTO.setDna(Arrays.asList(new DnaDetail(1L),new DnaDetail(2L),
            new DnaDetail(3L),new DnaDetail(4L)));
        mockMvcHelper("/api/dna/save",dnaDTO).andExpect(status().isOk());
    }

    @Test
    @Transactional
    @Sql("/data/musicDna.sql")
    public void notMatchDataTest() throws Exception {
        User user=usersRepository.findByEmail_Email("test1@example.com").get();
        DnaDto dnaDTO=new DnaDto(user.getUserId());
        dnaDTO.setDna(Arrays.asList(new DnaDetail(1L),new DnaDetail(2L),
                new DnaDetail(3L)));
        mockMvcHelper("/api/dna/save",dnaDTO).andExpect(status().isBadRequest());
    }
}

