package org.cosmic.backend.domainsTest.albumChat.albumLike;

import lombok.extern.log4j.Log4j2;
import org.cosmic.backend.domain.albumChat.dtos.albumChat.AlbumChatResponse;
import org.cosmic.backend.domain.albumChat.dtos.albumlike.AlbumChatAlbumLikeDto;
import org.cosmic.backend.domain.albumChat.dtos.albumlike.AlbumChatAlbumLikeReq;
import org.cosmic.backend.domain.albumChat.repositorys.AlbumChatRepository;
import org.cosmic.backend.domain.auth.dtos.UserLogin;
import org.cosmic.backend.domain.playList.domains.Album;
import org.cosmic.backend.domain.playList.domains.Artist;
import org.cosmic.backend.domain.playList.repositorys.AlbumRepository;
import org.cosmic.backend.domain.playList.repositorys.ArtistRepository;
import org.cosmic.backend.domain.playList.repositorys.TrackRepository;
import org.cosmic.backend.domain.post.dtos.Post.AlbumDto;
import org.cosmic.backend.domain.user.domains.User;
import org.cosmic.backend.domain.user.repositorys.EmailRepository;
import org.cosmic.backend.domain.user.repositorys.UsersRepository;
import org.cosmic.backend.domainsTest.BaseSetting;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.time.Instant;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Log4j2
public class DeleteAlbumLikeTest extends BaseSetting {
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
    public void albumlikeDeleteTest() throws Exception {
        UserLogin userLogin = loginUser("test@example.com");
        String validToken=userLogin.getToken();
        User user=getUser();
        Instant now = Instant.now();

        Artist artist=saveArtist("비비");

        Album album=saveAlbum("밤양갱", artist, now);

        saveAlbumChat(artist, album, now);
        resultActions =mockMvc.perform(MockMvcRequestBuilders.post("/api/albumchat/open")
            .header("Authorization", "Bearer " + validToken)
            .contentType("application/json")
            .content(mapper.writeValueAsString(AlbumDto.builder()
                .albumId(album.getAlbumId())
                .build()
            )));

        result = resultActions.andReturn();

        String content = result.getResponse().getContentAsString();
        AlbumChatResponse albumChatResponse = mapper.readValue(content, AlbumChatResponse.class);
        Long albumChatId = albumChatResponse.getAlbumChatId();
        System.out.println("albumChatId:"+albumChatId);

        resultActions =mockMvc.perform(MockMvcRequestBuilders.post("/api/albumchat/albumlike/create")
            .header("Authorization", "Bearer " + validToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(AlbumChatAlbumLikeDto.builder()
                .userId(user.getUserId())
                .albumChatId(albumChatId)
                .build()
            )));

        result = resultActions.andReturn();

        content = result.getResponse().getContentAsString();
        AlbumChatAlbumLikeReq albumChatAlbumLikeReq = mapper.readValue(content, AlbumChatAlbumLikeReq.class);
        Long albumChatAlbumLikeId = albumChatAlbumLikeReq.getAlbumChatAlbumLikeId();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/albumchat/albumlike/delete")
                .header("Authorization", "Bearer " + validToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(AlbumChatAlbumLikeReq.builder()
                    .albumChatAlbumLikeId(albumChatAlbumLikeId)
                    .build()
                ))).andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void notMatchAlbumlikeDeleteTest() throws Exception {
        UserLogin userLogin = loginUser("test@example.com");
        String validToken=userLogin.getToken();
        User user=getUser();
        Instant now = Instant.now();

        Artist artist=saveArtist("비비");

        Album album=saveAlbum("밤양갱", artist, now);

        saveAlbumChat(artist, album, now);
        resultActions =mockMvc.perform(MockMvcRequestBuilders.post("/api/albumchat/open")
            .header("Authorization", "Bearer " + validToken)
            .contentType("application/json")
            .content(mapper.writeValueAsString(AlbumDto.builder()
                .albumId(album.getAlbumId())
                .build()
            )));

        result = resultActions.andReturn();

        String content = result.getResponse().getContentAsString();
        AlbumChatResponse albumChatResponse = mapper.readValue(content, AlbumChatResponse.class);
        Long albumChatId = albumChatResponse.getAlbumChatId();
        System.out.println("albumChatId:"+albumChatId);

        resultActions =mockMvc.perform(MockMvcRequestBuilders.post("/api/albumchat/albumlike/create")
            .header("Authorization", "Bearer " + validToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(AlbumChatAlbumLikeDto.builder()
                .userId(user.getUserId())
                .albumChatId(albumChatId)
                .build()
            )));

        result = resultActions.andReturn();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/albumchat/albumlike/delete")
            .header("Authorization", "Bearer " + validToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(AlbumChatAlbumLikeReq.builder()
                .albumChatAlbumLikeId(100L)
                .build()
            ))).andDo(print())
            .andExpect(status().isNotFound());
    }
}
