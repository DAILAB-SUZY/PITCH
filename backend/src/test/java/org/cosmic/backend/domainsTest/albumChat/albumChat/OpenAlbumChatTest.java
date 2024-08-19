package org.cosmic.backend.domainsTest.albumChat.albumChat;

import lombok.extern.log4j.Log4j2;
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
import org.cosmic.backend.domainsTest.BaseSetting;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.time.Instant;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Log4j2
public class OpenAlbumChatTest extends BaseSetting {
    @Autowired
    private MockMvc mockMvc;
    final ObjectMapper mapper = new ObjectMapper();
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

    @Test
    @Transactional
    public void albumChatOpenTest() throws Exception {
        UserLogin userLogin = loginUser("test@example.com");
        String validToken=userLogin.getToken();
        Instant now = Instant.now();

        Artist artist=saveArtist("비비");

        Album album=saveAlbum("밤양갱", artist, now);

        saveAlbumChat(artist, album,now);
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
        UserLogin userLogin = loginUser("test@example.com");
        String validToken=userLogin.getToken();
        Instant now = Instant.now();

        Artist artist=saveArtist("비비");

        Album album=saveAlbum("밤양갱", artist, now);

        saveAlbumChat(artist, album,now);

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
