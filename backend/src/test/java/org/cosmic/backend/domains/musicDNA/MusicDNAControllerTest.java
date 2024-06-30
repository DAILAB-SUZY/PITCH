package org.cosmic.backend.domains.musicDNA;

import lombok.extern.log4j.Log4j2;
import org.cosmic.backend.domain.musicDNA.domain.MusicDna;
import org.cosmic.backend.domain.musicDNA.dto.DNADetail;
import org.cosmic.backend.domain.musicDNA.dto.DnaDTO;
import org.cosmic.backend.domain.musicDNA.repository.EmotionRepository;
import org.cosmic.backend.domain.user.repository.EmailRepository;
import org.cosmic.backend.domain.user.repository.UsersRepository;
import org.cosmic.backend.domains.BaseSetting;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.Arrays;
import java.util.List;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Log4j2
public class MusicDNAControllerTest extends BaseSetting {
    @Autowired
    private EmailRepository emailRepository;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private MockMvc mockMvc;

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    ObjectMapper mapper = new ObjectMapper();
    @Autowired
    private EmotionRepository emotionRepository;

    //DNA데이터 잘 주는지
    @Test
    public void request_DNATest() throws Exception {
        List<MusicDna> DNA= Arrays.asList(new MusicDna("느긋한"),new MusicDna("신나는"),new MusicDna("조용한"),new MusicDna("청순한"));
        for(int i=0;i<4;i++)
        {
            emotionRepository.save(DNA.get(i));
        }
        mockMvc.perform(post("/api/dna/give")
        .contentType("application/json")
        .content(mapper.writeValueAsString(DNA)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].emotion").value("느긋한"))
        .andExpect(jsonPath("$[1].emotion").value("신나는"));
    }

    @Test
    public void saveDNATest() throws Exception {
        List<MusicDna> TestDNA= Arrays.asList(new MusicDna("느긋한"),new MusicDna("신나는"),new MusicDna("조용한"),new MusicDna("청순한"));
        for(int i=0;i<4;i++)
        {
            emotionRepository.save(TestDNA.get(i));
        }
        DnaDTO dnaDTO=new DnaDTO();
        dnaDTO.setKey(1L);
        dnaDTO.setDna(Arrays.asList(new DNADetail(1L),new DNADetail(2L),new DNADetail(3L),new DNADetail(4L)));

        RegisterUser("testmans@naver.com");

        mockMvc.perform(post("/api/dna/save")
        .contentType("application/json")
        .content(mapper.writeValueAsString(DnaDTO.builder()
                .key(dnaDTO.getKey())
                .dna(dnaDTO.getDna())
                .build()
             )))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    public void notMatchDataTest() throws Exception {
        List<MusicDna> TestDNA= Arrays.asList(new MusicDna("느긋한"),new MusicDna("신나는"),new MusicDna("청순한"));
        for(int i=0;i<3;i++)
        {
            emotionRepository.save(TestDNA.get(i));
        }
        DnaDTO dnaDTO=new DnaDTO();
        dnaDTO.setKey(1L);
        dnaDTO.setDna(Arrays.asList(new DNADetail(1L),new DNADetail(2L),new DNADetail(3L)));
        RegisterUser("testboys@naver.com");

        mockMvc.perform(post("/api/dna/save")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(DnaDTO.builder()
                                .key(dnaDTO.getKey())
                                .dna(dnaDTO.getDna())
                                .build()
                        )))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}

