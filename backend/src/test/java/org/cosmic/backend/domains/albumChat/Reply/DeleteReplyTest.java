package org.cosmic.backend.domains.albumChat.Reply;

import lombok.extern.log4j.Log4j2;
import org.cosmic.backend.domain.albumChat.domain.AlbumChat;
import org.cosmic.backend.domain.albumChat.dto.albumChat.AlbumChatResponse;
import org.cosmic.backend.domain.albumChat.dto.comment.AlbumChatCommentDto;
import org.cosmic.backend.domain.albumChat.dto.comment.CreateAlbumChatCommentReq;
import org.cosmic.backend.domain.albumChat.dto.reply.AlbumChatReplyDto;
import org.cosmic.backend.domain.albumChat.dto.reply.CreateAlbumChatReplyReq;
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

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Log4j2
public class DeleteReplyTest extends BaseSetting {
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
    public void DeletereplyTest() throws Exception {
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
        AlbumChatResponse albumChatResponse = mapper.readValue(content, AlbumChatResponse.class); // 응답 JSON을 PostDto 객체로 변환
        Long albumChatId = albumChatResponse.getAlbumChatId();

        resultActions=mockMvc.perform(MockMvcRequestBuilders.post("/api/albumchat/comment/create")
                .header("Authorization", "Bearer " + validToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(CreateAlbumChatCommentReq.builder()
                        .userId(user.getUserId())
                        .albumChatId(albumChatId)
                        .content("안녕")
                        .createTime(null)
                        .build()
                )));
        result = resultActions.andReturn();

        content = result.getResponse().getContentAsString();
        AlbumChatCommentDto albumChatCommentDto = mapper.readValue(content, AlbumChatCommentDto.class); // 응답 JSON을 PostDto 객체로 변환
        Long albumChatCommentId = albumChatCommentDto.getAlbumChatCommentId();

        resultActions=mockMvc.perform(MockMvcRequestBuilders.post("/api/albumchat/reply/create")
                        .header("Authorization", "Bearer " + validToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(CreateAlbumChatReplyReq.builder()
                                .albumChatCommentId(albumChatCommentId)
                                .content("hello")
                                .createTime(null)
                                .userId(user.getUserId())
                                .build()
                        )));

        result = resultActions.andReturn();

        content = result.getResponse().getContentAsString();
        AlbumChatReplyDto albumChatReplyDto = mapper.readValue(content, AlbumChatReplyDto.class); // 응답 JSON을 PostDto 객체로 변환
        Long albumChatReplyId = albumChatReplyDto.getAlbumChatReplyId();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/albumchat/reply/delete")
                .header("Authorization", "Bearer " + validToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(AlbumChatReplyDto.builder()
                        .albumChatReplyId(albumChatReplyId)
                        .build()
                ))).andDo(print())
                .andExpect(status().isOk());
    }
    @Test
    @Transactional
    public void DeleteNotMatchReplyTest() throws Exception {
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
        AlbumChatResponse albumChatResponse = mapper.readValue(content, AlbumChatResponse.class); // 응답 JSON을 PostDto 객체로 변환
        Long albumChatId = albumChatResponse.getAlbumChatId();

        resultActions=mockMvc.perform(MockMvcRequestBuilders.post("/api/albumchat/comment/create")
                .header("Authorization", "Bearer " + validToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(CreateAlbumChatCommentReq.builder()
                        .userId(user.getUserId())
                        .albumChatId(albumChatId)
                        .content("안녕")
                        .createTime(null)
                        .build()
                )));
        result = resultActions.andReturn();

        content = result.getResponse().getContentAsString();
        AlbumChatCommentDto albumChatCommentDto = mapper.readValue(content, AlbumChatCommentDto.class); // 응답 JSON을 PostDto 객체로 변환
        Long albumChatCommentId = albumChatCommentDto.getAlbumChatCommentId();

        resultActions=mockMvc.perform(MockMvcRequestBuilders.post("/api/albumchat/reply/create")
                .header("Authorization", "Bearer " + validToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(CreateAlbumChatReplyReq.builder()
                        .albumChatCommentId(albumChatCommentId)
                        .content("hello")
                        .createTime(null)
                        .userId(user.getUserId())
                        .build()
                )));

        result = resultActions.andReturn();

        content = result.getResponse().getContentAsString();
        AlbumChatReplyDto albumChatReplyDto = mapper.readValue(content, AlbumChatReplyDto.class); // 응답 JSON을 PostDto 객체로 변환
        Long albumChatReplyId = albumChatReplyDto.getAlbumChatReplyId();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/albumchat/reply/delete")
                        .header("Authorization", "Bearer " + validToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(AlbumChatReplyDto.builder()
                                .albumChatReplyId(100L)
                                .build()
                        ))).andDo(print())
                .andExpect(status().isNotFound());
    }
}
