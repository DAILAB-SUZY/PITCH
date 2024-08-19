package org.cosmic.backend.domainsTest.albumChat.albumChat;

import lombok.extern.log4j.Log4j2;
import org.cosmic.backend.domain.albumChat.dtos.albumChat.AlbumChatDto;
import org.cosmic.backend.domain.albumChat.dtos.albumChat.AlbumChatResponse;
import org.cosmic.backend.domain.albumChat.dtos.comment.AlbumChatCommentCreateReq;
import org.cosmic.backend.domain.albumChat.dtos.comment.AlbumChatCommentDto;
import org.cosmic.backend.domain.albumChat.dtos.commentlike.AlbumChatCommentLikeDto;
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

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Log4j2
public class ManyLikeAlbumChatCommentTest extends BaseSetting {
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

    @Test
    @Transactional
    public void manyLikeAlbumChatCommentTest() throws Exception {
        UserLogin userLogin = loginUser("test2@example.com");
        String validToken=userLogin.getToken();
        Instant now = Instant.now();
        User user=getUser();
        User user2=getUser2();
        Artist artist=saveArtist("비비");

        Album album=saveAlbum("밤양갱", artist, now);

        saveAlbumChat(artist, album,now);
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/albumchat/open")
                        .header("Authorization", "Bearer " + validToken)
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(AlbumDto.builder()
                                .albumId(album.getAlbumId())
                                .build()
                        )))
                .andDo(print())
                .andExpect(status().isOk());
        MvcResult result = resultActions.andReturn();

        String content = result.getResponse().getContentAsString();
        AlbumChatResponse albumChatResponse = mapper.readValue(content, AlbumChatResponse.class);
        Long albumChatId = albumChatResponse.getAlbumChatId();

        resultActions =mockMvc.perform(MockMvcRequestBuilders.post("/api/albumchat/comment/create")
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
        AlbumChatCommentDto albumChatCommentDto2 = mapper.readValue(content, AlbumChatCommentDto.class);
        Long albumChatCommentId2 = albumChatCommentDto2.getAlbumChatCommentId();

        mockMvc.perform(post("/api/albumchat/commentlike/create")
            .header("Authorization", "Bearer " + validToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(AlbumChatCommentLikeDto.builder()
                .albumChatCommentId(albumChatCommentId2)
                .userId(user.getUserId())
                .build()
            )));

        resultActions =mockMvc.perform(MockMvcRequestBuilders.post("/api/albumchat/comment/create")
            .header("Authorization", "Bearer " + validToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(AlbumChatCommentCreateReq.builder()
                .userId(user2.getUserId())
                .albumChatId(albumChatId)
                .content("안녕")
                .createTime(null)
                .build()
            )));
        result = resultActions.andReturn();

        content = result.getResponse().getContentAsString();
        AlbumChatCommentDto albumChatCommentDto = mapper.readValue(content, AlbumChatCommentDto.class); // 응답 JSON을 PostDto 객체로 변환
        Long albumChatCommentId = albumChatCommentDto.getAlbumChatCommentId();


        mockMvc.perform(post("/api/albumchat/commentlike/create")
            .header("Authorization", "Bearer " + validToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(AlbumChatCommentLikeDto.builder()
                .albumChatCommentId(albumChatCommentId)
                .userId(user.getUserId())
                .build()
            )));

        mockMvc.perform(post("/api/albumchat/commentlike/create")
            .header("Authorization", "Bearer " + validToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(AlbumChatCommentLikeDto.builder()
                .albumChatCommentId(albumChatCommentId)
                .userId(user2.getUserId())
                .build()
            )));


        mockMvc.perform(post("/api/albumchat/manylike")
                .header("Authorization", "Bearer " + validToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(AlbumChatDto.builder()
                    .albumChatId(albumChatId)
                    .build()
                ))
            ).andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].albumChatCommentId", is(2)))
            .andExpect(jsonPath("$[1].albumChatCommentId", is(1))) ;
        }
    }

