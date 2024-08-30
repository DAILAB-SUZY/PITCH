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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Log4j2
public class ManyLikeAlbumChatCommentTest extends BaseSetting {
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
    @Sql("/data/albumChat.sql")
    public void manyLikeAlbumChatCommentTest() throws Exception {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        User user=userRepository.findByEmail_Email("test1@example.com").get();
        user.setPassword(encoder.encode(user.getPassword()));
        User user2=userRepository.findByEmail_Email("test2@example.com").get();
        user2.setPassword(encoder.encode(user2.getPassword()));
        UserLogin userLogin = loginUser("test1@example.com");
        Album album=albumRepository.findByTitleAndArtist_ArtistName("bam","bibi").get();

        AlbumDto albumDto = AlbumDto.createAlbumDto(album.getAlbumId());
        ResultActions resultActions =mockMvcHelper("/api/albumchat/open",albumDto);
        MvcResult result = resultActions.andReturn();
        String content = result.getResponse().getContentAsString();
        AlbumChatResponse albumChatResponse = mapper.readValue(content, AlbumChatResponse.class);
        Long albumChatId = albumChatResponse.getAlbumChatId();

        AlbumChatCommentCreateReq albumChatCommentCreateReq=AlbumChatCommentCreateReq.createAlbumChatCommentCreateReq
            (user.getUserId(),albumChatId,"안녕",null);
        resultActions =mockMvcHelper("/api/albumchat/comment/create",albumChatCommentCreateReq);
        result = resultActions.andReturn();
        content = result.getResponse().getContentAsString();
        AlbumChatCommentDto albumChatCommentDto2 = mapper.readValue(content, AlbumChatCommentDto.class);
        Long albumChatCommentId2 = albumChatCommentDto2.getAlbumChatCommentId();

        AlbumChatCommentLikeDto albumChatCommentLikeDto=AlbumChatCommentLikeDto.createAlbumChatCommentLikeDto
            (user.getUserId(),albumChatCommentId2);
        mockMvcHelper("/api/albumchat/commentlike/create",albumChatCommentLikeDto);

        AlbumChatCommentCreateReq albumChatCommentCreateReq1=AlbumChatCommentCreateReq.createAlbumChatCommentCreateReq
            (user2.getUserId(),albumChatId,"안녕",null);
        resultActions=mockMvcHelper("/api/albumchat/comment/create",albumChatCommentCreateReq1);
        result = resultActions.andReturn();
        content = result.getResponse().getContentAsString();
        AlbumChatCommentDto albumChatCommentDto = mapper.readValue(content, AlbumChatCommentDto.class); // 응답 JSON을 PostDto 객체로 변환
        Long albumChatCommentId = albumChatCommentDto.getAlbumChatCommentId();

        albumChatCommentLikeDto=AlbumChatCommentLikeDto.createAlbumChatCommentLikeDto
            (user.getUserId(),albumChatCommentId);
        mockMvcHelper("/api/albumchat/commentlike/create",albumChatCommentLikeDto);
        albumChatCommentLikeDto=AlbumChatCommentLikeDto.createAlbumChatCommentLikeDto
            (user2.getUserId(),albumChatCommentId);
        mockMvcHelper("/api/albumchat/commentlike/create",albumChatCommentLikeDto);

        AlbumChatDto albumChatDto=AlbumChatDto.createAlbumChatDto(albumChatId);
        mockMvcHelper("/api/albumchat/manylike",albumChatDto)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].albumChatCommentId", is(2)))
            .andExpect(jsonPath("$[1].albumChatCommentId", is(1)));
        }
    }

