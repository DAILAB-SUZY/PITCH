package org.cosmic.backend.domains.favoriteArtist;

import lombok.extern.log4j.Log4j2;
import org.cosmic.backend.domain.albumChat.repositorys.AlbumChatRepository;
import org.cosmic.backend.domain.auth.dtos.UserLogin;
import org.cosmic.backend.domain.favoriteArtist.dtos.FavoriteReq;
import org.cosmic.backend.domain.playList.domains.Album;
import org.cosmic.backend.domain.playList.domains.Artist;
import org.cosmic.backend.domain.playList.domains.Track;
import org.cosmic.backend.domain.playList.repositorys.AlbumRepository;
import org.cosmic.backend.domain.playList.repositorys.ArtistRepository;
import org.cosmic.backend.domain.playList.repositorys.TrackRepository;
import org.cosmic.backend.domain.user.domains.User;
import org.cosmic.backend.domain.user.dtos.UserDto;
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
public class GiveFavoriteArtistTest extends BaseSetting {
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
    public void favoriteArtistGiveTest() throws Exception {
        UserLogin userLogin = loginUser("test@example.com", "12345678");
        String validToken = userLogin.getToken();
        Instant now = Instant.now();
        User user=getUser();
        Artist artist = saveArtist("비비");

        Album album = saveAlbum("밤양갱", artist, now, "발라드");

        Track track = saveTrack("밤양갱", album, artist, now, "발라드");

        mockMvc.perform(post("/api/favoriteArtist/save")
            .header("Authorization", "Bearer " + validToken)
            .contentType("application/json")
            .content(mapper.writeValueAsString(FavoriteReq.builder()
                .albumId(album.getAlbumId())
                .artistId(artist.getArtistId())
                .cover(album.getCover())
                .trackId(track.getTrackId())
                .userId(user.getUserId())
                .build()
            )));

        mockMvc.perform(post("/api/favoriteArtist/give")
                .header("Authorization", "Bearer " + validToken)
                .contentType("application/json")
                .content(mapper.writeValueAsString(UserDto.builder()
                    .userId(user.getUserId())
                    .build()
                )))
            .andDo(print())
            .andExpect(status().isOk());
    }
    @Test
    @Transactional
    public void notMatchFavoriteArtistGiveTest() throws Exception {
        UserLogin userLogin = loginUser("test@example.com", "12345678");
        String validToken = userLogin.getToken();
        Instant now = Instant.now();
        User user=getUser();
        Artist artist = saveArtist("비비");

        Album album = saveAlbum("밤양갱", artist, now, "발라드");

        Track track = saveTrack("밤양갱", album, artist, now, "발라드");

        mockMvc.perform(post("/api/favoriteArtist/save")
            .header("Authorization", "Bearer " + validToken)
            .contentType("application/json")
            .content(mapper.writeValueAsString(FavoriteReq.builder()
                .albumId(album.getAlbumId())
                .artistId(artist.getArtistId())
                .cover(album.getCover())
                .trackId(track.getTrackId())
                .userId(user.getUserId())
                .build()
            )));
        mockMvc.perform(post("/api/favoriteArtist/give")
                .header("Authorization", "Bearer " + validToken)
                .contentType("application/json")
                .content(mapper.writeValueAsString(UserDto.builder()
                    .userId(100L)
                    .build()
                )))
            .andDo(print())
            .andExpect(status().isNotFound());
    }
}
