package org.cosmic.backend.domains.albumChat.albumChat;

import lombok.extern.log4j.Log4j2;
import org.cosmic.backend.domain.albumChat.domains.AlbumChat;
import org.cosmic.backend.domain.albumChat.repositorys.AlbumChatRepository;
import org.cosmic.backend.domain.auth.dtos.UserLogin;
import org.cosmic.backend.domain.playList.domains.Album;
import org.cosmic.backend.domain.playList.domains.Artist;
import org.cosmic.backend.domain.playList.repositorys.AlbumRepository;
import org.cosmic.backend.domain.playList.repositorys.ArtistRepository;
import org.cosmic.backend.domain.playList.repositorys.TrackRepository;
import org.cosmic.backend.domain.post.dtos.Post.AlbumDto;
import org.cosmic.backend.domain.user.repositorys.EmailRepository;
import org.cosmic.backend.domain.user.repositorys.UsersRepository;
import org.cosmic.backend.domains.BaseSetting;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.time.Instant;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Log4j2
public class OpenAlbumChatTest extends BaseSetting {
    @Autowired
    private MockMvc mockMvc;
    ObjectMapper mapper = new ObjectMapper();
    @Autowired
    UsersRepository userRepository;
    @Autowired
    EmailRepository emailRepository;
    @Autowired
    ArtistRepository artistRepository;
    @Autowired
    AlbumRepository albumRepository;
    @Autowired
    TrackRepository trackRepository;
    @Autowired
    AlbumChatRepository albumChatRepository;

    private ResultActions resultActions;
    private MvcResult result;

    @Test
    @Transactional
    public void albumChatOpenTest() throws Exception {
        UserLogin userLogin = loginUser("test@example.com","12345678");
        String validToken=userLogin.getToken();
        Instant now = Instant.now();

        Artist artist=saveArtist("비비");

        Album album=saveAlbum("밤양갱", artist, now, "발라드");

        AlbumChat albumChat= saveAlbumChat("밤양갱", artist, album,now, "발라드");

        mockMvc.perform(post("/api/albumchat/open")
                .header("Authorization", "Bearer " + validToken)
                .contentType("application/json")
                .content(mapper.writeValueAsString(AlbumDto.builder()
                    .albumId(album.getAlbumId())
                    .build()
                )))
            .andDo(print())
            .andExpect(status().isOk());
    }
    //아티스트 또는 앨범으로 찾기

    @Test
    @Transactional
    public void albumChatNotOpenTest() throws Exception {
        UserLogin userLogin = loginUser("test@example.com","12345678");
        String validToken=userLogin.getToken();
        Instant now = Instant.now();

        Artist artist=saveArtist("비비");

        Album album=saveAlbum("밤양갱", artist, now, "발라드");

        AlbumChat albumChat= saveAlbumChat("밤양갱", artist, album,now, "발라드");

        mockMvc.perform(post("/api/albumchat/open")
                        .header("Authorization", "Bearer " + validToken)
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(AlbumDto.builder()
                                .albumId(100L)
                                .build()
                        )))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}
