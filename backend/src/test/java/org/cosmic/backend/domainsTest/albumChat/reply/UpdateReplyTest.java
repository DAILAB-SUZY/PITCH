package org.cosmic.backend.domainsTest.albumChat.reply;

import lombok.extern.log4j.Log4j2;
import org.cosmic.backend.domain.albumChat.dtos.albumChat.AlbumChatResponse;
import org.cosmic.backend.domain.albumChat.dtos.comment.AlbumChatCommentCreateReq;
import org.cosmic.backend.domain.albumChat.dtos.comment.AlbumChatCommentDto;
import org.cosmic.backend.domain.albumChat.dtos.reply.AlbumChatReplyCreateReq;
import org.cosmic.backend.domain.albumChat.dtos.reply.AlbumChatReplyDto;
import org.cosmic.backend.domain.albumChat.dtos.reply.AlbumChatReplyUpdateReq;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Log4j2
public class UpdateReplyTest extends BaseSetting {
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
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Test
    @Transactional
    @Sql("/data/albumChat.sql")
    public void replyUpdateTest() throws Exception {
        User user=userRepository.findByEmail_Email("test1@example.com").get();
        user.setPassword(encoder.encode(user.getPassword()));
        UserLogin userLogin = loginUser("test1@example.com");
        Album album=albumRepository.findByTitleAndArtist_ArtistName("bam","bibi").get();

        AlbumDto albumDto = AlbumDto.createAlbumDto(album.getAlbumId());
        ResultActions resultActions =mockMvcHelper("/api/albumchat/open",albumDto);
        MvcResult result = resultActions.andReturn();
        String content = result.getResponse().getContentAsString();
        AlbumChatResponse albumChatResponse = mapper.readValue(content, AlbumChatResponse.class);
        Long albumChatId = albumChatResponse.getAlbumChatId();

        AlbumChatCommentCreateReq albumChatCommentCreateReq=AlbumChatCommentCreateReq.createAlbumChatCommentCreateReq(
            user.getUserId(),albumChatId,"안녕",null);
        resultActions=mockMvcHelper("/api/albumchat/comment/create",albumChatCommentCreateReq);
        result = resultActions.andReturn();
        content = result.getResponse().getContentAsString();
        AlbumChatCommentDto albumChatCommentDto = mapper.readValue(content, AlbumChatCommentDto.class);
        Long albumChatCommentId = albumChatCommentDto.getAlbumChatCommentId();

        AlbumChatReplyCreateReq albumChatReplyCreateReq=AlbumChatReplyCreateReq.createAlbumChatReplyCreateReq(
            user.getUserId(),albumChatCommentId,"hello",null);
        resultActions=mockMvcHelper("/api/albumchat/reply/create",albumChatReplyCreateReq);
        result = resultActions.andReturn();
        content = result.getResponse().getContentAsString();
        AlbumChatReplyDto albumChatReplyDto = mapper.readValue(content, AlbumChatReplyDto.class);
        Long albumChatReplyId = albumChatReplyDto.getAlbumChatReplyId();

        AlbumChatReplyUpdateReq albumChatReplyUpdateReq=AlbumChatReplyUpdateReq.createAlbumChatReplyUpdateReq(
            user.getUserId(),albumChatCommentId,albumChatReplyId,"hello",null);
        mockMvcHelper("/api/albumchat/reply/update",albumChatReplyUpdateReq).andExpect(status().isOk());
    }

    @Test
    @Transactional
    @Sql("/data/albumChat.sql")
    public void notMatchReplyUpdateTest() throws Exception {
        User user=userRepository.findByEmail_Email("test1@example.com").get();
        user.setPassword(encoder.encode(user.getPassword()));
        UserLogin userLogin = loginUser("test1@example.com");
        Album album=albumRepository.findByTitleAndArtist_ArtistName("bam","bibi").get();

        AlbumDto albumDto = AlbumDto.createAlbumDto(album.getAlbumId());
        ResultActions resultActions =mockMvcHelper("/api/albumchat/open",albumDto);
        MvcResult result = resultActions.andReturn();
        String content = result.getResponse().getContentAsString();
        AlbumChatResponse albumChatResponse = mapper.readValue(content, AlbumChatResponse.class);
        Long albumChatId = albumChatResponse.getAlbumChatId();

        AlbumChatCommentCreateReq albumChatCommentCreateReq=AlbumChatCommentCreateReq.createAlbumChatCommentCreateReq(
            user.getUserId(),albumChatId,"안녕",null);
        resultActions=mockMvcHelper("/api/albumchat/comment/create",albumChatCommentCreateReq);
        result = resultActions.andReturn();
        content = result.getResponse().getContentAsString();
        AlbumChatCommentDto albumChatCommentDto = mapper.readValue(content, AlbumChatCommentDto.class);
        Long albumChatCommentId = albumChatCommentDto.getAlbumChatCommentId();

        AlbumChatReplyCreateReq albumChatReplyCreateReq=AlbumChatReplyCreateReq.createAlbumChatReplyCreateReq(
                user.getUserId(),albumChatCommentId,"hello",null);
        mockMvcHelper("/api/albumchat/reply/create",albumChatReplyCreateReq);

        AlbumChatReplyUpdateReq albumChatReplyUpdateReq=AlbumChatReplyUpdateReq.createAlbumChatReplyUpdateReq(
            user.getUserId(),albumChatCommentId,100L,"hello",null);
        mockMvcHelper("/api/albumchat/reply/update",albumChatReplyUpdateReq).andExpect(status().isNotFound());
    }
}
