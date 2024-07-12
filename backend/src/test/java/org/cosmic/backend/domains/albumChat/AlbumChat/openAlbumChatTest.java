package org.cosmic.backend.domains.albumChat.AlbumChat;

import lombok.extern.log4j.Log4j2;
import org.cosmic.backend.domain.albumChat.domain.AlbumChat;
import org.cosmic.backend.domain.albumChat.repository.AlbumChatRepository;
import org.cosmic.backend.domain.auth.dto.UserLogin;
import org.cosmic.backend.domain.playList.domain.Album;
import org.cosmic.backend.domain.playList.domain.Artist;
import org.cosmic.backend.domain.playList.repository.AlbumRepository;
import org.cosmic.backend.domain.playList.repository.ArtistRepository;
import org.cosmic.backend.domain.playList.repository.TrackRepository;
import org.cosmic.backend.domain.post.dto.Post.AlbumDto;
import org.cosmic.backend.domain.user.domain.Email;
import org.cosmic.backend.domain.user.domain.User;
import org.cosmic.backend.domain.user.repository.EmailRepository;
import org.cosmic.backend.domain.user.repository.UsersRepository;
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
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.time.Instant;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Log4j2
public class openAlbumChatTest {
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
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        Email email = emailRepository.save(Email.builder()
                .email("testmand@example.com")
                .verificationCode("12345678")
                .verified(true)
                .build());

        User user=userRepository.save(User.builder()
                .email(email)
                .username("goodwill")
                .password(encoder.encode("12345678"))
                .build());

        UserLogin userLogin = UserLogin.builder()
                .email("testmand@example.com")
                .password("12345678")
                .build();

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(userLogin)));

        MvcResult result = resultActions.andReturn();
        String validToken = mapper.readValue(result.getResponse().getContentAsString(), UserLogin.class).getToken();

        Instant now = Instant.now();

        Artist artist=artistRepository.save(Artist.builder()
                .artistName("비비")
                .build());
        Album album=albumRepository.save(Album.builder()
                .title("밤양갱")
                .cover("base")
                .artist(artist)
                .createdDate(now)
                .genre("발라드")
                .build());

        AlbumChat albumChat= albumChatRepository.save(AlbumChat.builder()
                .CreateTime(now)
                .genre("발라드")
                .cover("base")
                .title("밤양갱")
                .album(album)
                .artistName("비비")
                .build()
        );

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
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        Email email = emailRepository.save(Email.builder()
                .email("testmand@example.com")
                .verificationCode("12345678")
                .verified(true)
                .build());

        User user=userRepository.save(User.builder()
                .email(email)
                .username("goodwill")
                .password(encoder.encode("12345678"))
                .build());

        UserLogin userLogin = UserLogin.builder()
                .email("testmand@example.com")
                .password("12345678")
                .build();

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(userLogin)));

        MvcResult result = resultActions.andReturn();
        String validToken = mapper.readValue(result.getResponse().getContentAsString(), UserLogin.class).getToken();

        Instant now = Instant.now();

        Artist artist=artistRepository.save(Artist.builder()
                .artistName("비비")
                .build());
        Album album=albumRepository.save(Album.builder()
                .title("밤양갱")
                .cover("base")
                .artist(artist)
                .createdDate(now)
                .genre("발라드")
                .build());

        AlbumChat albumChat= albumChatRepository.save(AlbumChat.builder()
                .CreateTime(now)
                .genre("발라드")
                .cover("base")
                .title("밤양갱")
                .album(album)
                .artistName("비비")
                .build()
        );

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
