package org.cosmic.backend.domainsTest.playlist;

import lombok.extern.log4j.Log4j2;
import org.cosmic.backend.domain.auth.dtos.UserLogin;
import org.cosmic.backend.domain.musicDna.repositorys.EmotionRepository;
import org.cosmic.backend.domain.playList.domains.Album;
import org.cosmic.backend.domain.playList.domains.Artist;
import org.cosmic.backend.domain.playList.domains.Playlist;
import org.cosmic.backend.domain.playList.domains.Track;
import org.cosmic.backend.domain.playList.dtos.ArtistDto;
import org.cosmic.backend.domain.playList.dtos.PlaylistDetail;
import org.cosmic.backend.domain.playList.dtos.PlaylistDto;
import org.cosmic.backend.domain.playList.dtos.TrackDto;
import org.cosmic.backend.domain.playList.repositorys.AlbumRepository;
import org.cosmic.backend.domain.playList.repositorys.ArtistRepository;
import org.cosmic.backend.domain.playList.repositorys.PlaylistRepository;
import org.cosmic.backend.domain.playList.repositorys.TrackRepository;
import org.cosmic.backend.domain.user.domains.Email;
import org.cosmic.backend.domain.user.domains.User;
import org.cosmic.backend.domain.user.repositorys.EmailRepository;
import org.cosmic.backend.domain.user.repositorys.UsersRepository;
import org.cosmic.backend.domainsTest.BaseSetting;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Log4j2
public class CreatePlaylistTest extends BaseSetting {
    @Autowired
    private MockMvc mockMvc;
    final ObjectMapper mapper = new ObjectMapper();
    @Autowired
    EmotionRepository emotionRepository;
    @Autowired
    UsersRepository userRepository;
    @Autowired
    EmailRepository emailRepository;
    @Autowired
    ArtistRepository artistRepository;
    @Autowired
    AlbumRepository albumRepository;
    @Autowired
    PlaylistRepository playlistRepository;
    @Autowired
    TrackRepository trackRepository;

    @Test
    public void playlistSaveTest() throws Exception {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        Email email = emailRepository.save(Email.builder()
            .email("2@example.com")
            .verificationCode("12345678")
            .verified(true)
            .build());

        User user=userRepository.save(User.builder()
            .email(email)
            .username("goodwill")
            .password(encoder.encode("12345678"))
            .build());


        Instant now = Instant.now();

        Artist artist=artistRepository.save(Artist.builder()
            .artistName("비비1")
            .build());
        Album album=albumRepository.save(Album.builder()
            .title("밤양갱1")
            .cover("base")
            .artist(artist)
            .createdDate(now)
            .genre("발라드")
            .build());

        Track track=trackRepository.save(Track.builder()
            .title("밤양갱1")
            .album(album)
            .Cover("base")
            .artist(artist)
            .createdDate(now)
            .genre("발라드")
            .build());

        Playlist playlist=playlistRepository.save(Playlist.builder()
            .createdDate(now)
            .updatedDate(now)
            .user(user)
            .build());

        UserLogin userLogin = UserLogin.builder()
            .email("2@example.com")
            .password("12345678")
            .build();

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/auth/signin")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(userLogin)));

        MvcResult result = resultActions.andReturn();
        String validToken = mapper.readValue(result.getResponse().getContentAsString(), UserLogin.class).getToken();

        PlaylistDto playlistdto=new PlaylistDto();
        playlistdto.setId(user.getUserId());
        playlistdto.setPlaylist(List.of(new PlaylistDetail(track.getTrackId())));

        mockMvc.perform(post("/api/playlist/save")
                .header("Authorization", "Bearer " + validToken)
                .contentType("application/json")
                .content(mapper.writeValueAsString(PlaylistDto.builder()
                    .id(playlistdto.getId())
                    .playlist(playlistdto.getPlaylist())
                    .build()
                )))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    public void rightInfoFindTest() throws Exception {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        Email email = emailRepository.save(Email.builder()
            .email("3@example.com")
            .verificationCode("12345678")
            .verified(true)
            .build());

        User user=userRepository.save(User.builder()
            .email(email)
            .username("goodwill")
            .password(encoder.encode("12345678"))
            .profilePicture("base")
            .signupDate(Instant.now())
            .build());
        Instant now = Instant.now();

        Artist artist=artistRepository.save(Artist.builder()
            .artistName("비비2")
            .build());


        Album album=albumRepository.save(Album.builder()
            .title("밤양갱2")
            .cover("base")
            .artist(artist)
            .createdDate(now)
            .genre("발라드")
            .build());


        Track track=trackRepository.save(Track.builder()
            .title("밤양갱2")
            .album(album)
            .Cover("base")
            .artist(artist)
            .createdDate(now)
            .genre("발라드")
            .build());

        Playlist playlist=playlistRepository.save(Playlist.builder()
            .createdDate(now)
            .updatedDate(now)
            .user(user)
            .build());
        UserLogin userLogin = UserLogin.builder()
            .email("3@example.com")
            .password("12345678")
            .build();

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/auth/signin")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(userLogin)));

        MvcResult result = resultActions.andReturn();
        String validToken = mapper.readValue(result.getResponse().getContentAsString(), UserLogin.class).getToken();

        mockMvc.perform(post("/api/playlist/Tracksearch")
                .header("Authorization", "Bearer " + validToken)
                .contentType("application/json")
                .content(mapper.writeValueAsString(TrackDto.builder()
                    .trackName(track.getTitle())
                    .build()
                )))
            .andDo(print())
            .andExpect(status().isOk());

        mockMvc.perform(post("/api/playlist/Artistsearch")
                .header("Authorization", "Bearer " + validToken)
                .contentType("application/json")
                .content(mapper.writeValueAsString(ArtistDto.builder()
                    .artistName("비비2")
                    .build()
                )))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    public void wrongInfoFindTest() throws Exception {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        Email email = emailRepository.save(Email.builder()
            .email("4@example.com")
            .verificationCode("12345678")
            .verified(true)
            .build());

        User user=userRepository.save(User.builder()
            .email(email)
            .username("goodwill")
            .password(encoder.encode("12345678"))
            .profilePicture("base")
            .signupDate(Instant.now())
            .build());
        Instant now = Instant.now();

        Artist artist=artistRepository.save(Artist.builder()
            .artistName("비비3")
            .build());

        Album album=albumRepository.save(Album.builder()
            .title("밤양갱3")
            .cover("base")
            .artist(artist)
            .createdDate(now)
            .genre("발라드")
            .build());
        Track track=trackRepository.save(Track.builder()
            .title("밤양갱3")
            .album(album)
            .Cover("base")
            .artist(artist)
            .createdDate(now)
            .genre("발라드")
            .build());

        Playlist playlist=playlistRepository.save(Playlist.builder()
            .createdDate(now)
            .updatedDate(now)
            .user(user)
            .build());
        UserLogin userLogin = UserLogin.builder()
            .email("4@example.com")
            .password("12345678")
            .build();

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/auth/signin")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(userLogin)));

        MvcResult result = resultActions.andReturn();
        String validToken = mapper.readValue(result.getResponse().getContentAsString(), UserLogin.class).getToken();

        mockMvc.perform(post("/api/playlist/Tracksearch")
                .header("Authorization", "Bearer " + validToken)
                .contentType("application/json")
                .content(mapper.writeValueAsString(TrackDto.builder()
                    .trackName("hi")
                    .build()
                )))
            .andDo(print())
            .andExpect(status().isNotFound());

        mockMvc.perform(post("/api/playlist/Artistsearch")
                    .header("Authorization", "Bearer " + validToken)
                    .contentType("application/json")
                    .content(mapper.writeValueAsString(ArtistDto.builder()
                        .artistName("아이")
                        .build()
                    )))
                .andDo(print())
                .andExpect(status().isNotFound());
            }
}
