package org.cosmic.backend.domains.favoriteArtist;

import lombok.extern.log4j.Log4j2;
import org.cosmic.backend.domain.albumChat.repositorys.AlbumChatRepository;
import org.cosmic.backend.domain.auth.dtos.UserLogin;
import org.cosmic.backend.domain.favoriteArtist.dtos.AlbumRequest;
import org.cosmic.backend.domain.favoriteArtist.dtos.TrackRequest;
import org.cosmic.backend.domain.playList.domains.Album;
import org.cosmic.backend.domain.playList.domains.Artist;
import org.cosmic.backend.domain.playList.domains.Track;
import org.cosmic.backend.domain.playList.dtos.ArtistDto;
import org.cosmic.backend.domain.playList.repositorys.AlbumRepository;
import org.cosmic.backend.domain.playList.repositorys.ArtistRepository;
import org.cosmic.backend.domain.playList.repositorys.TrackRepository;
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

    private ResultActions resultActions;
    private MvcResult result;

    @Test
    @Transactional
    public void artistSearchMatchTest() throws Exception {
        UserLogin userLogin = loginUser("test@example.com");
        String validToken=userLogin.getToken();
        Instant now = Instant.now();

        Artist artist=saveArtist("비비");

        Album album=saveAlbum("밤양갱", artist, now);

        Track track=saveTrack(album,artist,now);

        mockMvc.perform(post("/api/favoriteArtist/searchartist")
                .header("Authorization", "Bearer " + validToken)
                .contentType("application/json")
                .content(mapper.writeValueAsString(ArtistDto.builder()
                    .artistName(artist.getArtistName())
                    .build()
                )))
            .andDo(print())
            .andExpect(status().isOk());
    }
    @Test
    @Transactional
    public void notMatchArtistSearchTest() throws Exception {
        UserLogin userLogin = loginUser("test@example.com");
        String validToken=userLogin.getToken();
        Instant now = Instant.now();

        Artist artist=saveArtist("비비");

        Album album=saveAlbum("밤양갱", artist, now);

        Track track=saveTrack(album,artist,now);

        mockMvc.perform(post("/api/favoriteArtist/searchartist")
                .header("Authorization", "Bearer " + validToken)
                .contentType("application/json")
                .content(mapper.writeValueAsString(ArtistDto.builder()
                    .artistName("비")
                    .build()
                )))
            .andDo(print())
            .andExpect(status().isNotFound());
    }
    @Test
    @Transactional
    public void albumSearchMatchTest() throws Exception {
        UserLogin userLogin = loginUser("test@example.com");
        String validToken=userLogin.getToken();
        Instant now = Instant.now();

        Artist artist=saveArtist("비비");

        Album album=saveAlbum("밤양갱", artist, now);

        Track track=saveTrack(album,artist,now);

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
        UserLogin userLogin = loginUser("test@example.com");
        String validToken=userLogin.getToken();
        Instant now = Instant.now();

        Artist artist=saveArtist("비비");

        Album album=saveAlbum("밤양갱", artist, now);

        Track track=saveTrack(album,artist,now);

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
    public void trackSearchMatchTest() throws Exception {
        UserLogin userLogin = loginUser("test@example.com");
        String validToken=userLogin.getToken();
        Instant now = Instant.now();

        Artist artist=saveArtist("비비");

        Album album=saveAlbum("밤양갱", artist, now);

        Track track=saveTrack(album,artist,now);

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
        UserLogin userLogin = loginUser("test@example.com");
        String validToken=userLogin.getToken();
        Instant now = Instant.now();

        Artist artist=saveArtist("비비");

        Album album=saveAlbum("밤양갱", artist, now);

        Track track=saveTrack(album,artist,now);

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
