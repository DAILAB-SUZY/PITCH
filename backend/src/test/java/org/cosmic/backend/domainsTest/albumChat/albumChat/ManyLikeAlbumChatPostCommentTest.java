package org.cosmic.backend.domainsTest.albumChat.albumChat;

import lombok.extern.log4j.Log4j2;
import org.cosmic.backend.domain.albumChat.dtos.albumChat.AlbumChatDto;
import org.cosmic.backend.domain.albumChat.dtos.comment.AlbumChatCommentDto;
import org.cosmic.backend.domain.albumChat.dtos.comment.AlbumChatCommentReq;
import org.cosmic.backend.domain.albumChat.dtos.commentlike.AlbumChatCommentLikeDto;
import org.cosmic.backend.domain.auth.dtos.UserLogin;
import org.cosmic.backend.domain.playList.domains.Album;
import org.cosmic.backend.domain.playList.repositorys.AlbumRepository;
import org.cosmic.backend.domain.playList.repositorys.ArtistRepository;
import org.cosmic.backend.domain.playList.repositorys.TrackRepository;
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
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.data.redis.connection.ReactiveStringCommands.BitOpCommand.perform;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.*;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Log4j2
public class ManyLikeAlbumChatPostCommentTest extends BaseSetting {
    final ObjectMapper mapper = new ObjectMapper();
    @Autowired

    private MockMvc mockMvc;
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

    @Test
    @Transactional
    @Sql("/data/albumChat.sql")
    public void manyLikeAlbumChatCommentTest() throws Exception {

        //USER1, 2의  valid token때문에 생긴 문제 각각의 valid token필요.

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        User user=userRepository.findByEmail_Email("test1@example.com").get();
        user.setPassword(encoder.encode(user.getPassword()));
        UserLogin userLogin = loginUser("test1@example.com");

        User user2=userRepository.findByEmail_Email("test2@example.com").get();
        user2.setPassword(encoder.encode(user2.getPassword()));
        UserLogin userLogin2 = loginUser("test2@example.com");

        Album album=albumRepository.findByTitleAndArtist_ArtistName("bam","bibi").get();

        AlbumChatCommentReq albumChatCommentReq=AlbumChatCommentReq.createAlbumChatCommentReq(
            "안녕",null);
        ResultActions resultActions =mockMvcHelper("/api/album/{albumId}/comment"
            ,album.getAlbumId(),albumChatCommentReq,userLogin.getToken());
        MvcResult result = resultActions.andReturn();
        String content = result.getResponse().getContentAsString();
        AlbumChatCommentDto albumChatCommentDto2 = mapper.readValue(content, AlbumChatCommentDto.class);
        Long albumChatCommentId1 = albumChatCommentDto2.getAlbumChatCommentId();

        mockMvcsHelper("/api/album/{albumId}/comment/{albumChatCommentId}/commentLike",
            album.getAlbumId(),albumChatCommentId1,userLogin.getToken());

        //---

        resultActions =mockMvcHelper("/api/album/{albumId}/comment"
            ,album.getAlbumId(),albumChatCommentReq,userLogin.getToken());
        result = resultActions.andReturn();
        content = result.getResponse().getContentAsString();
        AlbumChatCommentDto albumChatCommentDto = mapper.readValue(content, AlbumChatCommentDto.class);
        Long albumChatCommentId2 = albumChatCommentDto.getAlbumChatCommentId();

        mockMvcsHelper("/api/album/{albumId}/comment/{albumChatCommentId}/commentLike",
                album.getAlbumId(),albumChatCommentId2,userLogin.getToken());//user1

        mockMvcsHelper("/api/album/{albumId}/comment/{albumChatCommentId}/commentLike",
                album.getAlbumId(),albumChatCommentId2,userLogin2.getToken());//user2

        mockMvcGetHelper("/api/album/{albumId}?sorted=manylike",album.getAlbumId(),userLogin.getToken())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].albumChatCommentId", is(2)))
            .andExpect(jsonPath("$[1].albumChatCommentId", is(1)));
        }
    }

