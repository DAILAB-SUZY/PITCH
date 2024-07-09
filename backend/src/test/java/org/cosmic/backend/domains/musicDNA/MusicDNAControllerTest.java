package org.cosmic.backend.domains.musicDNA;

import lombok.extern.log4j.Log4j2;
import org.cosmic.backend.domain.musicDNA.domain.MusicDna;
import org.cosmic.backend.domain.musicDNA.dto.DNADetail;
import org.cosmic.backend.domain.musicDNA.dto.DnaDTO;
import org.cosmic.backend.domain.musicDNA.repository.EmotionRepository;
import org.cosmic.backend.domain.user.domain.Email;
import org.cosmic.backend.domain.user.domain.User;
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
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        Email email = emailRepository.save(Email.builder()
                .email("testu1@naver.com")
                .verificationCode("12345678")
                .verified(true)
                .build());

        User user=usersRepository.save(User.builder()
                .email(email)
                .username("goodwill")
                .password(encoder.encode("12345678"))
                .build());

        DnaDTO dnaDTO=new DnaDTO();
        dnaDTO.setKey(user.getUserId());
        dnaDTO.setDna(Arrays.asList(new DNADetail(1L),new DNADetail(2L),new DNADetail(3L),new DNADetail(4L)));

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
        System.out.println("*****1");
        for(int i=0;i<3;i++)
        {
            emotionRepository.save(TestDNA.get(i));
        }
        System.out.println("*****2");
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        Email email = emailRepository.save(Email.builder()
                .email("testu2@naver.com")
                .verificationCode("12345678")
                .verified(true)
                .build());

        User user=usersRepository.save(User.builder()
                .email(email)
                .username("goodwill")
                .password(encoder.encode("12345678"))
                .build());

        System.out.println("*****2");
        DnaDTO dnaDTO=new DnaDTO();
        dnaDTO.setKey(user.getUserId());
        dnaDTO.setDna(Arrays.asList(new DNADetail(1L),new DNADetail(2L),new DNADetail(3L)));
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

