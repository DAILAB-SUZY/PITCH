package org.cosmic.backend.domains.favoriteArtist;

import lombok.extern.log4j.Log4j2;
import org.cosmic.backend.domain.albumChat.domain.AlbumChat;
import org.cosmic.backend.domain.albumChat.repository.AlbumChatRepository;
import org.cosmic.backend.domain.auth.dto.UserLogin;
import org.cosmic.backend.domain.favoriteArtist.dto.AlbumRequest;
import org.cosmic.backend.domain.favoriteArtist.dto.TrackRequest;
import org.cosmic.backend.domain.playList.domain.Album;
import org.cosmic.backend.domain.playList.domain.Artist;
import org.cosmic.backend.domain.playList.domain.Track;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.time.Instant;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Log4j2
public class SearchFavoriteArtistTest extends BaseSetting {
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
    public void matchArtistSearchTest() throws Exception {
        UserLogin userLogin = loginUser("test@example.com","12345678");
        String validToken=userLogin.getToken();
        Instant now = Instant.now();

        Artist artist=saveArtist("비비");

        Album album=saveAlbum("밤양갱", artist, now, "발라드");

        Track track=saveTrack("밤양갱",album,artist,now,"발라드");

        mockMvc.perform(post("/api/favoriteArtist/searchartist")
                        .header("Authorization", "Bearer " + validToken)
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(ArtistDTO.builder()
                                .artistName(artist.getArtistName())
                                .build()
                        )))
                .andDo(print())
                .andExpect(status().isOk());
    }
    @Test
    @Transactional
    public void notMatchArtistSearchTest() throws Exception {
        UserLogin userLogin = loginUser("test@example.com","12345678");
        String validToken=userLogin.getToken();
        Instant now = Instant.now();

        Artist artist=saveArtist("비비");

        Album album=saveAlbum("밤양갱", artist, now, "발라드");

        Track track=saveTrack("밤양갱",album,artist,now,"발라드");

        mockMvc.perform(post("/api/favoriteArtist/searchartist")
                        .header("Authorization", "Bearer " + validToken)
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(ArtistDTO.builder()
                                .artistName("비")
                                .build()
                        )))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
    @Test
    @Transactional
    public void matchAlbumSearchTest() throws Exception {
        UserLogin userLogin = loginUser("test@example.com","12345678");
        String validToken=userLogin.getToken();
        Instant now = Instant.now();

        Artist artist=saveArtist("비비");

        Album album=saveAlbum("밤양갱", artist, now, "발라드");

        Track track=saveTrack("밤양갱",album,artist,now,"발라드");

        mockMvc.perform(post("/api/favoriteArtist/searchalbum")
                        .header("Authorization", "Bearer " + validToken)
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(AlbumRequest.builder()
                                .artistId(artist.getArtistId())
                                .albumName(album.getTitle())
                                .build()
                        )))
                .andDo(print())
                .andExpect(status().isOk());
    }
    @Test
    @Transactional
    public void notMatchAlbumSearchTest() throws Exception {
        UserLogin userLogin = loginUser("test@example.com","12345678");
        String validToken=userLogin.getToken();
        Instant now = Instant.now();

        Artist artist=saveArtist("비비");

        Album album=saveAlbum("밤양갱", artist, now, "발라드");

        Track track=saveTrack("밤양갱",album,artist,now,"발라드");

        mockMvc.perform(post("/api/favoriteArtist/searchalbum")
                        .header("Authorization", "Bearer " + validToken)
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(AlbumRequest.builder()
                                .artistId(artist.getArtistId())
                                .albumName("밤양")
                                .build()
                        )))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void matchTrackSearchTest() throws Exception {
        UserLogin userLogin = loginUser("test@example.com","12345678");
        String validToken=userLogin.getToken();
        Instant now = Instant.now();

        Artist artist=saveArtist("비비");

        Album album=saveAlbum("밤양갱", artist, now, "발라드");

        Track track=saveTrack("밤양갱",album,artist,now,"발라드");

        mockMvc.perform(post("/api/favoriteArtist/searchtrack")
                        .header("Authorization", "Bearer " + validToken)
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(TrackRequest.builder()
                                .trackName(track.getTitle())
                                .albumId(album.getAlbumId())
                                .build()
                        )))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void notMatchTrackSearchTest() throws Exception {
        UserLogin userLogin = loginUser("test@example.com","12345678");
        String validToken=userLogin.getToken();
        Instant now = Instant.now();

        Artist artist=saveArtist("비비");

        Album album=saveAlbum("밤양갱", artist, now, "발라드");

        Track track=saveTrack("밤양갱",album,artist,now,"발라드");

        mockMvc.perform(post("/api/favoriteArtist/searchtrack")
                        .header("Authorization", "Bearer " + validToken)
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(TrackRequest.builder()
                                .trackName("밤갱")
                                .albumId(album.getAlbumId())
                                .build()
                        )))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}
