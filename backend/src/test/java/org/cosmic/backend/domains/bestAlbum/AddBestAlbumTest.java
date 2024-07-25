package org.cosmic.backend.domains.bestAlbum;
import lombok.extern.log4j.Log4j2;
import org.cosmic.backend.domain.auth.dtos.UserLogin;
import org.cosmic.backend.domain.bestAlbum.dtos.BestAlbumDto;
import org.cosmic.backend.domain.playList.domain.Album;
import org.cosmic.backend.domain.playList.domain.Artist;
import org.cosmic.backend.domain.playList.repository.AlbumRepository;
import org.cosmic.backend.domain.playList.repository.ArtistRepository;
import org.cosmic.backend.domain.playList.repository.TrackRepository;
import org.cosmic.backend.domain.user.domains.User;
import org.cosmic.backend.domain.user.repositorys.EmailRepository;
import org.cosmic.backend.domain.user.repositorys.UsersRepository;
import org.cosmic.backend.domains.BaseSetting;
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
public class AddBestAlbumTest extends BaseSetting {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    EmailRepository emailRepository;
    @Autowired
    UsersRepository userRepository;
    @Autowired
    TrackRepository trackRepository;
    @Autowired
    AlbumRepository albumRepository;
    @Autowired
    ArtistRepository artistRepository;

    ObjectMapper mapper = new ObjectMapper();

    @Test
    @Transactional
    public void addAlbumTest() throws Exception {
        UserLogin userLogin = loginUser("test@example.com","12345678");
        User user=getUser();
        String validToken=userLogin.getToken();
        Instant now = Instant.now();

        Artist artist=saveArtist("비비");

        Album album=saveAlbum("밤양갱", artist, now, "발라드");

        mockMvc.perform(post("/api/bestAlbum/add")
                        .header("Authorization", "Bearer " + validToken)
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(BestAlbumDto.builder()
                                .userId(user.getUserId())
                                .albumId(album.getAlbumId())
                                .build()
                        )))
                .andDo(print())
                .andExpect(status().isOk());
    }
    @Test
    @Transactional
    public void addNotMatchAlbumTest() throws Exception {
        UserLogin userLogin = loginUser("test@example.com","12345678");
        User user=getUser();
        String validToken=userLogin.getToken();
        Instant now = Instant.now();

        Artist artist=saveArtist("비비");

        Album album=saveAlbum("밤양갱", artist, now, "발라드");

        mockMvc.perform(post("/api/bestAlbum/add")
                        .header("Authorization", "Bearer " + validToken)
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(BestAlbumDto.builder()
                                .userId(100L)
                                .albumId(album.getAlbumId())
                                .build()
                        )))
                .andDo(print())
                .andExpect(status().isNotFound());
        mockMvc.perform(post("/api/bestAlbum/add")
                        .header("Authorization", "Bearer " + validToken)
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(BestAlbumDto.builder()
                                .userId(user.getUserId())
                                .albumId(100L)
                                .build()
                        )))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
    // 유효한 상태에서 잘 만들어지는지

}
