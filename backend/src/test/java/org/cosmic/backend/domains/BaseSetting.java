package org.cosmic.backend.domains;

import lombok.extern.log4j.Log4j2;
import org.cosmic.backend.domain.albumChat.domains.AlbumChat;
import org.cosmic.backend.domain.albumChat.repositorys.AlbumChatRepository;
import org.cosmic.backend.domain.auth.dtos.UserLogin;
import org.cosmic.backend.domain.playList.domain.*;
import org.cosmic.backend.domain.playList.repository.*;
import org.cosmic.backend.domain.user.domains.Email;
import org.cosmic.backend.domain.user.domains.User;
import org.cosmic.backend.domain.user.repositorys.EmailRepository;
import org.cosmic.backend.domain.user.repositorys.UsersRepository;
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Log4j2
public class BaseSetting {
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected UsersRepository userRepository;
    @Autowired
    protected EmailRepository emailRepository;
    @Autowired
    protected ArtistRepository artistRepository;
    @Autowired
    protected AlbumRepository albumRepository;
    @Autowired
    protected TrackRepository trackRepository;
    @Autowired
    protected AlbumChatRepository albumChatRepository;
    @Autowired
    protected PlaylistRepository playlistRepository;

    protected ObjectMapper mapper = new ObjectMapper();
    private User user;
    private User user2;

    protected UserLogin loginUser(String email, String password) throws Exception {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        Email savedEmail = emailRepository.save(Email.builder()
                .email(email)
                .verificationCode("12345678")
                .verified(true)
                .build());

        User savedUser = userRepository.save(User.builder()
                .email(savedEmail)
                .username("goodwill")
                .password(encoder.encode(password))
                .build());
        user=savedUser;

        UserLogin userLogin = UserLogin.builder()
                .email(email)
                .password(password)
                .build();

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(userLogin)));

        MvcResult result = resultActions.andReturn();
        String validToken = mapper.readValue(result.getResponse().getContentAsString(), UserLogin.class).getToken();

        return UserLogin.builder()
                .email(email)
                .token(validToken)
                .build();
    }

    protected UserLogin loginUser2(String email, String password) throws Exception {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        Email savedEmail2 = emailRepository.save(Email.builder()
                .email("test@example.com")
                .verificationCode("12345678")
                .verified(true)
                .build());

        User savedUser2 = userRepository.save(User.builder()
                .email(savedEmail2)
                .username("goodwill")
                .password(encoder.encode(password))
                .build());
        user2=savedUser2;

        UserLogin userLogin = UserLogin.builder()
                .email("test@example.com")
                .password(password)
                .build();

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(userLogin)));

        MvcResult result = resultActions.andReturn();
        String validToken = mapper.readValue(result.getResponse().getContentAsString(), UserLogin.class).getToken();

        return UserLogin.builder()
                .email("test@example.com")
                .token(validToken)
                .build();
    }

    protected User getUser() throws Exception {
        return user;
    }
    protected User getUser2() throws Exception {
        return user2;
    }

    protected Artist saveArtist(String artistName) {
        return artistRepository.save(Artist.builder()
                .artistName(artistName)
                .build());
    }

    protected Album saveAlbum(String title, Artist artist, Instant createdDate, String genre) {
        return albumRepository.save(Album.builder()
                .title(title)
                .cover("base")
                .artist(artist)
                .createdDate(createdDate)
                .genre(genre)
                .build());
    }

    protected AlbumChat saveAlbumChat(String title, Artist artist, Album album,Instant createdDate, String genre) {
        return albumChatRepository.save(AlbumChat.builder()
                .CreateTime(createdDate)
                .genre("발라드")
                .cover("base")
                .title("밤양갱")
                .album(album)
                .artistName("비비")
                .build());
    }

    protected Track saveTrack(String title, Album album, Artist artist, Instant createdDate, String genre) {
        return trackRepository.save(Track.builder()
                .title(title)
                .album(album)
                .Cover("base")
                .artist(artist)
                .createdDate(createdDate)
                .genre(genre)
                .build());
    }
    protected Playlist savePlaylist(Instant createdDate,User user,Instant updatedDate) {
        return playlistRepository.save(Playlist.builder()
                .createdDate(createdDate)
                .user(user)
                .updatedDate(updatedDate)
                .build());
    }
}
