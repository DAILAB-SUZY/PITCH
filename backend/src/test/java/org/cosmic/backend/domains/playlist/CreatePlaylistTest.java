package org.cosmic.backend.domains.playlist;

import lombok.extern.log4j.Log4j2;
import org.cosmic.backend.domain.playList.dto.playlistDTO;
import org.cosmic.backend.domain.playList.dto.playlistDetail;
import org.cosmic.backend.domain.user.domain.User;
import org.cosmic.backend.domain.user.dto.userDto;
import org.cosmic.backend.domains.BaseSetting;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Log4j2
public class CreatePlaylistTest extends BaseSetting {
    @Autowired
    private MockMvc mockMvc;
    ObjectMapper mapper = new ObjectMapper();

    @Test
    public void savePlaylistTest() throws Exception {
        User newuser=DNASetting("testgirls@naver.com");//DNA들 넣어놓고 user등록 후 넣는 과정까지
        PlaylistSetting();//playlist 넣어놓고 각 앨범, 아티스트, 노래들의 연관관계 연결

        String validToken = GiveToken();

        //User의 dna를 설정하고 user_dna를 가져옴
        playlistDTO playlistdto=new playlistDTO();
        playlistdto.setId(1L);
        playlistdto.setPlaylist(Arrays.asList(new playlistDetail(1L)));

        mockMvc.perform(post("/api/playlist/save")
                .header("Authorization", "Bearer " + validToken)
                .contentType("application/json")
                .content(mapper.writeValueAsString(playlistDTO.builder()
                        .id(playlistdto.getId())
                        .playlist(playlistdto.getPlaylist())
                        .build()
                )))
            .andDo(print())
            .andExpect(status().isOk());

        //저장 후 열람
        mockMvc.perform(post("/api/playlist/give")
                .header("Authorization", "Bearer " + validToken)
                .contentType("application/json")
                .content(mapper.writeValueAsString(userDto.builder()
                        .userid(1L)
                        .build()))) // build() 메서드를 호출하여 최종 객체를 생성
            .andExpect(status().isOk());
    }
    // 유효한 상태에서 잘 만들어지는지
}
