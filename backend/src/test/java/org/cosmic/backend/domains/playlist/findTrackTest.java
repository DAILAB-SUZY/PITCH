package org.cosmic.backend.domains.playlist;

import lombok.extern.log4j.Log4j2;
import org.cosmic.backend.domain.playList.dto.ArtistDTO;
import org.cosmic.backend.domain.playList.dto.trackDTO;
import org.cosmic.backend.domain.user.domain.User;
import org.cosmic.backend.domains.BaseSetting;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Log4j2
public class findTrackTest extends BaseSetting {
    @Autowired
    private MockMvc mockMvc;
    ObjectMapper mapper = new ObjectMapper();

    @Test
    public void findWrongInfoTest() throws Exception {
        User newuser=DNASetting("testbois@naver.com");//DNA들 넣어놓고 user등록 후 넣는 과정까지
        PlaylistSetting();//playlist 넣어놓고 각 앨범, 아티스트, 노래들의 연관관계 연결
        String validToken = GiveToken();

        mockMvc.perform(post("/api/playlist/Tracksearch")
                        .header("Authorization", "Bearer " + validToken)
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(trackDTO.builder()
                                .trackName("hi")
                                .build()
                        )))
                .andDo(print())
                .andExpect(status().isUnauthorized());

        mockMvc.perform(post("/api/playlist/Artistsearch")
                        .header("Authorization", "Bearer " + validToken)
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(ArtistDTO.builder()
                                .artistName("아이")
                                .build()
                        )))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void findRightInfoTest() throws Exception {
        User newuser=DNASetting("testboi@naver.com");//DNA들 넣어놓고 user등록 후 넣는 과정까지
        PlaylistSetting();//playlist 넣어놓고 각 앨범, 아티스트, 노래들의 연관관계 연결

        String validToken = GiveToken();
        mockMvc.perform(post("/api/playlist/Tracksearch")
                        .header("Authorization", "Bearer " + validToken)
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(trackDTO.builder()
                                .trackName("밤양갱")
                                .build()
                        )))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/playlist/Artistsearch")
                        .header("Authorization", "Bearer " + validToken)
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(ArtistDTO.builder()
                                .artistName("비비")
                                .build()
                        )))
                .andDo(print())
                .andExpect(status().isOk());
    }
    // 유효한 상태에서 잘 만들어지는지
}
