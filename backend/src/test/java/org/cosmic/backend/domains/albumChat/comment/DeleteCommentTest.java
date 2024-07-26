package org.cosmic.backend.domains.albumChat.Comment;

import lombok.extern.log4j.Log4j2;
import org.cosmic.backend.domain.albumChat.domains.AlbumChat;
import org.cosmic.backend.domain.albumChat.dtos.albumChat.AlbumChatResponse;
import org.cosmic.backend.domain.albumChat.dtos.comment.AlbumChatCommentDto;
import org.cosmic.backend.domain.albumChat.dtos.comment.AlbumChatCommentCreateReq;
import org.cosmic.backend.domain.albumChat.dtos.commentlike.AlbumChatCommentLikeDto;
import org.cosmic.backend.domain.albumChat.dtos.reply.AlbumChatReplyCreateReq;
import org.cosmic.backend.domain.albumChat.repositorys.AlbumChatRepository;
import org.cosmic.backend.domain.auth.dtos.UserLogin;
import org.cosmic.backend.domain.playList.domains.Album;
import org.cosmic.backend.domain.playList.domains.Artist;
import org.cosmic.backend.domain.playList.repositorys.AlbumRepository;
import org.cosmic.backend.domain.playList.repositorys.ArtistRepository;
import org.cosmic.backend.domain.playList.repositorys.TrackRepository;
import org.cosmic.backend.domain.post.dto.Post.AlbumDto;
import org.cosmic.backend.domain.user.domains.User;
import org.cosmic.backend.domain.user.repositorys.EmailRepository;
import org.cosmic.backend.domain.user.repositorys.UsersRepository;
import org.cosmic.backend.domains.BaseSetting;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Log4j2
public class DeleteCommentTest extends BaseSetting {
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
    public void commentDeleteTest() throws Exception {
        UserLogin userLogin = loginUser("test@example.com","12345678");
        String validToken=userLogin.getToken();
        User user=getUser();
        Instant now = Instant.now();

        Artist artist=saveArtist("비비");

        Album album=saveAlbum("밤양갱", artist, now, "발라드");

        AlbumChat albumChat= saveAlbumChat("밤양갱", artist, album,now, "발라드");

        resultActions=mockMvc.perform(MockMvcRequestBuilders.post("/api/albumchat/open")
                .header("Authorization", "Bearer " + validToken)
                .contentType("application/json")
                .content(mapper.writeValueAsString(AlbumDto.builder()
                    .albumId(album.getAlbumId())
                    .build()
                )))
            .andDo(print())
            .andExpect(status().isOk());
        result = resultActions.andReturn();

        String content = result.getResponse().getContentAsString();
        AlbumChatResponse albumChatResponse = mapper.readValue(content, AlbumChatResponse.class);
        Long albumChatId = albumChatResponse.getAlbumChatId();

        resultActions=mockMvc.perform(MockMvcRequestBuilders.post("/api/albumchat/comment/create")
            .header("Authorization", "Bearer " + validToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(AlbumChatCommentCreateReq.builder()
                .userId(user.getUserId())
                .albumChatId(albumChatId)
                .content("안녕")
                .createTime(null)
                .build()
            )));
        result = resultActions.andReturn();

        content = result.getResponse().getContentAsString();
        AlbumChatCommentDto albumChatCommentDto = mapper.readValue(content, AlbumChatCommentDto.class);

        mockMvc.perform(post("/api/albumchat/comment/delete")
                .header("Authorization", "Bearer " + validToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(albumChatCommentDto))).andDo(print())
            .andExpect(status().isOk());

    }

    @Test
    @Transactional
    public void notMatchCommentDeleteTest() throws Exception {
        UserLogin userLogin = loginUser("test@example.com","12345678");
        String validToken=userLogin.getToken();
        User user=getUser();
        Instant now = Instant.now();

        Artist artist=saveArtist("비비");

        Album album=saveAlbum("밤양갱", artist, now, "발라드");

        AlbumChat albumChat= saveAlbumChat("밤양갱", artist, album,now, "발라드");

        resultActions=mockMvc.perform(MockMvcRequestBuilders.post("/api/albumchat/open")
                .header("Authorization", "Bearer " + validToken)
                .contentType("application/json")
                .content(mapper.writeValueAsString(AlbumDto.builder()
                    .albumId(album.getAlbumId())
                    .build()
                )))
            .andDo(print())
            .andExpect(status().isOk());
        result = resultActions.andReturn();

        String content = result.getResponse().getContentAsString();
        AlbumChatResponse albumChatResponse = mapper.readValue(content, AlbumChatResponse.class);
        Long albumChatId = albumChatResponse.getAlbumChatId();

        resultActions=mockMvc.perform(MockMvcRequestBuilders.post("/api/albumchat/comment/create")
            .header("Authorization", "Bearer " + validToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(AlbumChatCommentCreateReq.builder()
                .userId(user.getUserId())
                .albumChatId(albumChatId)
                .content("안녕")
                .createTime(null)
                .build()
            )));
        result = resultActions.andReturn();

        content = result.getResponse().getContentAsString();
        AlbumChatCommentDto albumChatCommentDto = mapper.readValue(content, AlbumChatCommentDto.class);

        albumChatCommentDto.setAlbumChatCommentId(100L);

        mockMvc.perform(post("/api/albumchat/comment/delete")
                .header("Authorization", "Bearer " + validToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(albumChatCommentDto))).andDo(print())
            .andExpect(status().isNotFound());
    }
    @Test
    @Transactional
    public void commentLikeAndReplyDeleteTest() throws Exception {
        UserLogin userLogin = loginUser("test@example.com","12345678");
        String validToken=userLogin.getToken();
        User user=getUser();
        Instant now = Instant.now();

        Artist artist=saveArtist("비비");

        Album album=saveAlbum("밤양갱", artist, now, "발라드");

        AlbumChat albumChat= saveAlbumChat("밤양갱", artist, album,now, "발라드");
        resultActions=mockMvc.perform(MockMvcRequestBuilders.post("/api/albumchat/open")
                .header("Authorization", "Bearer " + validToken)
                .contentType("application/json")
                .content(mapper.writeValueAsString(AlbumDto.builder()
                .albumId(album.getAlbumId())
                .build()
                )))
            .andDo(print())
            .andExpect(status().isOk());
        result = resultActions.andReturn();

        String content = result.getResponse().getContentAsString();
        AlbumChatResponse albumChatResponse = mapper.readValue(content, AlbumChatResponse.class);
        Long albumChatId = albumChatResponse.getAlbumChatId();

        resultActions=mockMvc.perform(MockMvcRequestBuilders.post("/api/albumchat/comment/create")
            .header("Authorization", "Bearer " + validToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(AlbumChatCommentCreateReq.builder()
                .userId(user.getUserId())
                .albumChatId(albumChatId)
                .content("안녕")
                .createTime(null)
                .build()
            )));
        result = resultActions.andReturn();

        content = result.getResponse().getContentAsString();
        AlbumChatCommentDto albumChatCommentDto = mapper.readValue(content, AlbumChatCommentDto.class);
        Long albumChatCommentId = albumChatCommentDto.getAlbumChatCommentId();

        mockMvc.perform(post("/api/albumchat/reply/create")
            .header("Authorization", "Bearer " + validToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(AlbumChatReplyCreateReq.builder()
                .albumChatCommentId(albumChatCommentId)
                .content("hello")
                .createTime(null)
                .userId(user.getUserId())
                .build()
            )));
        mockMvc.perform(post("/api/albumchat/commentlike/create")
            .header("Authorization", "Bearer " + validToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(AlbumChatCommentLikeDto.builder()
                .albumChatCommentId(albumChatCommentId)
                .userId(user.getUserId())
                .build()
            )));

        mockMvc.perform(post("/api/albumchat/comment/delete")
            .header("Authorization", "Bearer " + validToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(albumChatCommentDto)));

        mockMvc.perform(post("/api/albumchat/reply/give")
                .header("Authorization", "Bearer " + validToken)
                .contentType("application/json")
                .content(mapper.writeValueAsString(AlbumChatCommentDto.builder()
                    .albumChatCommentId(albumChatCommentId)
                    .build()
                )))
            .andDo(print())
            .andExpect(status().isNotFound());

        mockMvc.perform(post("/api/albumchat/commentlike/give")
                .header("Authorization", "Bearer " + validToken)
                .contentType("application/json")
                .content(mapper.writeValueAsString(AlbumChatCommentDto.builder()
                    .albumChatCommentId(albumChatCommentId)
                    .build()
                )))
            .andDo(print())
            .andExpect(status().isNotFound());
    }


}