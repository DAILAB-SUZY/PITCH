package org.cosmic.backend.domains.albumChat.AlbumLike;

import lombok.extern.log4j.Log4j2;
import org.cosmic.backend.domain.albumChat.domains.AlbumChat;
import org.cosmic.backend.domain.albumChat.dtos.albumChat.AlbumChatDto;
import org.cosmic.backend.domain.albumChat.dtos.albumChat.AlbumChatResponse;
import org.cosmic.backend.domain.albumChat.dtos.albumlike.AlbumChatAlbumLikeDto;
import org.cosmic.backend.domain.albumChat.repositorys.AlbumChatRepository;
import org.cosmic.backend.domain.auth.dtos.UserLogin;
import org.cosmic.backend.domain.playList.domain.Album;
import org.cosmic.backend.domain.playList.domain.Artist;
import org.cosmic.backend.domain.playList.repository.AlbumRepository;
import org.cosmic.backend.domain.playList.repository.ArtistRepository;
import org.cosmic.backend.domain.playList.repository.TrackRepository;
import org.cosmic.backend.domain.post.dto.Post.AlbumDto;
import org.cosmic.backend.domain.user.domains.Email;
import org.cosmic.backend.domain.user.domains.User;
import org.cosmic.backend.domain.user.repositorys.EmailRepository;
import org.cosmic.backend.domain.user.repositorys.UsersRepository;
import org.cosmic.backend.domains.BaseSetting;
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

import java.time.Instant;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Log4j2
public class GiveAlbumLikeTest extends BaseSetting {

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
    public void giveAlbumLikesTest() throws Exception {
        UserLogin userLogin = loginUser("test@example.com","12345678");
        String validToken=userLogin.getToken();
        User user=getUser();
        Instant now = Instant.now();

        Artist artist=saveArtist("비비");

        Album album=saveAlbum("밤양갱", artist, now, "발라드");

        AlbumChat albumChat= saveAlbumChat("밤양갱", artist, album,now, "발라드");
        resultActions =mockMvc.perform(MockMvcRequestBuilders.post("/api/albumchat/open")
                .header("Authorization", "Bearer " + validToken)
                .contentType("application/json")
                .content(mapper.writeValueAsString(AlbumDto.builder()
                        .albumId(album.getAlbumId())
                        .build()
                )));

        result = resultActions.andReturn();

        String content = result.getResponse().getContentAsString();
        AlbumChatResponse albumChatResponse = mapper.readValue(content, AlbumChatResponse.class); // 응답 JSON을 PostDto 객체로 변환
        Long albumChatId = albumChatResponse.getAlbumChatId();
        System.out.println("albumChatId:"+albumChatId);

        mockMvc.perform(post("/api/albumchat/albumlike/create")
                        .header("Authorization", "Bearer " + validToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(AlbumChatAlbumLikeDto.builder()
                                .userId(user.getUserId())
                                .albumChatId(albumChatId)
                                .build()
                        )));

        mockMvc.perform(post("/api/albumchat/albumlike/give")
                        .header("Authorization", "Bearer " + validToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(AlbumChatDto.builder()
                                .albumChatId(albumChatId)
                                .build()
                        ))).andDo(print())
                .andExpect(status().isOk());
    }
}
