package org.cosmic.backend.domainsTest.albumChat.commentLike;

import lombok.extern.log4j.Log4j2;
import org.cosmic.backend.domain.albumChat.dtos.comment.AlbumChatCommentCreateReq;
import org.cosmic.backend.domain.albumChat.dtos.comment.AlbumChatCommentDto;
import org.cosmic.backend.domain.albumChat.dtos.commentlike.AlbumChatCommentLikeDto;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Log4j2
public class CreatePostCommentLikeTest extends BaseSetting {
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
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Test
    @Transactional
    @Sql("/data/albumChat.sql")
    public void commentLikeCreateTest() throws Exception {
        User user=userRepository.findByEmail_Email("test1@example.com").get();
        user.setPassword(encoder.encode(user.getPassword()));
        UserLogin userLogin = loginUser("test1@example.com");
        Album album=albumRepository.findByTitleAndArtist_ArtistName("bam","bibi").get();

        AlbumChatCommentCreateReq albumChatCommentCreateReq=AlbumChatCommentCreateReq.createAlbumChatCommentCreateReq(
            user.getUserId(),album.getAlbumId(),"안녕",null);
        ResultActions resultActions=mockMvcHelper("/api/albumchat/comment/create",albumChatCommentCreateReq);
        MvcResult result = resultActions.andReturn();
        String content = result.getResponse().getContentAsString();
        AlbumChatCommentDto albumChatCommentDto = mapper.readValue(content, AlbumChatCommentDto.class);
        Long albumChatCommentId = albumChatCommentDto.getAlbumChatCommentId();

        AlbumChatCommentLikeDto albumChatCommentLikeDto=AlbumChatCommentLikeDto.createAlbumChatCommentLikeDto(
            user.getUserId(),albumChatCommentId);
        mockMvcHelper("/api/albumchat/commentlike/create",albumChatCommentLikeDto)
            .andExpect(status().isOk());
    }

    @Test
    @Transactional
    @Sql("/data/albumChat.sql")
    public void notMatchCommentLikeCreateTest() throws Exception {
        User user=userRepository.findByEmail_Email("test1@example.com").get();
        user.setPassword(encoder.encode(user.getPassword()));
        UserLogin userLogin = loginUser("test1@example.com");
        Album album=albumRepository.findByTitleAndArtist_ArtistName("bam","bibi").get();

        AlbumChatCommentCreateReq albumChatCommentCreateReq=AlbumChatCommentCreateReq.createAlbumChatCommentCreateReq(
            user.getUserId(),album.getAlbumId(),"안녕",null);
        mockMvcHelper("/api/albumchat/comment/create",albumChatCommentCreateReq);

        AlbumChatCommentLikeDto albumChatCommentLikeDto=AlbumChatCommentLikeDto.createAlbumChatCommentLikeDto(
            user.getUserId(),100L
        );
        mockMvcHelper("/api/albumchat/commentlike/create",albumChatCommentLikeDto)
            .andExpect(status().isNotFound());
    }
    @Test
    @Transactional
    @Sql("/data/albumChat.sql")
    public void commentLikeExistTest() throws Exception {
        User user=userRepository.findByEmail_Email("test1@example.com").get();
        user.setPassword(encoder.encode(user.getPassword()));
        UserLogin userLogin = loginUser("test1@example.com");
        String validToken=userLogin.getToken();
        Album album=albumRepository.findByTitleAndArtist_ArtistName("bam","bibi").get();

        AlbumChatCommentCreateReq albumChatCommentCreateReq=AlbumChatCommentCreateReq.createAlbumChatCommentCreateReq(
            user.getUserId(),album.getAlbumId(),"안녕",null);
        ResultActions resultActions=mockMvcHelper("/api/albumchat/comment/create",albumChatCommentCreateReq);
        MvcResult result = resultActions.andReturn();
        String content = result.getResponse().getContentAsString();
        AlbumChatCommentDto albumChatCommentDto = mapper.readValue(content, AlbumChatCommentDto.class);
        Long albumChatCommentId = albumChatCommentDto.getAlbumChatCommentId();

        AlbumChatCommentLikeDto albumChatCommentLikeDto=AlbumChatCommentLikeDto.createAlbumChatCommentLikeDto(
            user.getUserId(),albumChatCommentId
        );
        mockMvcHelper("/api/albumchat/commentlike/create",albumChatCommentLikeDto);
        mockMvcHelper("/api/albumchat/commentlike/create",albumChatCommentLikeDto)
            .andExpect(status().isConflict());
    }
}
