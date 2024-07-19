package org.cosmic.backend.domains.bestAlbum;

import lombok.extern.log4j.Log4j2;
import org.cosmic.backend.domain.auth.dto.UserLogin;
import org.cosmic.backend.domain.bestAlbum.dto.AlbumDTO;
import org.cosmic.backend.domain.playList.domain.Album;
import org.cosmic.backend.domain.playList.domain.Artist;
import org.cosmic.backend.domain.playList.dto.ArtistDTO;
import org.cosmic.backend.domain.playList.repository.AlbumRepository;
import org.cosmic.backend.domain.playList.repository.ArtistRepository;
import org.cosmic.backend.domain.playList.repository.TrackRepository;
import org.cosmic.backend.domain.user.repository.EmailRepository;
import org.cosmic.backend.domain.user.repository.UsersRepository;
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
public class SearchAlbumTest extends BaseSetting {
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
    public void SearchAlbumNameTest() throws Exception {
        UserLogin userLogin = loginUser("test@example.com","12345678");
        String validToken=userLogin.getToken();
        Instant now = Instant.now();

        Artist artist=saveArtist("비비");

        Album album=saveAlbum("밤양갱", artist, now, "발라드");

        mockMvc.perform(post("/api/bestAlbum/Albumsearch")
                        .header("Authorization", "Bearer " + validToken)
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(AlbumDTO.builder()
                                .albumName("밤양갱")
                                .build()
                        )))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void SearchArtistNameTest() throws Exception {
        UserLogin userLogin = loginUser("test@example.com","12345678");
        String validToken=userLogin.getToken();
        Instant now = Instant.now();

        Artist artist=saveArtist("비비");

        Album album=saveAlbum("밤양갱", artist, now, "발라드");

        mockMvc.perform(post("/api/bestAlbum/Artistsearch")
                        .header("Authorization", "Bearer " + validToken)
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(ArtistDTO.builder()
                                .artistName("비비")
                                .build()
                        )))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
