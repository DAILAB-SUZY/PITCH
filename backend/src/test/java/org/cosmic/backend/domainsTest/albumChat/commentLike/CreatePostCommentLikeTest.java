package org.cosmic.backend.domainsTest.albumChat.commentLike;

import lombok.extern.log4j.Log4j2;
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
import org.cosmic.backend.domainsTest.UrlGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

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
    UrlGenerator urlGenerator=new UrlGenerator();
    Map<String,Object> params= new HashMap<>();

    @Test
    @Transactional
    @Sql("/data/albumChat.sql")
    public void commentLikeCreateTest() throws Exception {
        User user=userRepository.findByEmail_Email("test1@example.com").get();
        user.setPassword(encoder.encode(user.getPassword()));
        UserLogin userLogin = loginUser("test1@example.com");
        Album album=albumRepository.findByTitleAndArtist_ArtistName("bam","bibi").get();

        AlbumChatCommentReq albumChatCommentReq=AlbumChatCommentReq.createAlbumChatCommentReq(
                "안녕",null);
        params.clear();
        params.put("albumId",album.getAlbumId());
        String url=urlGenerator.buildUrl("/api/album/{albumId}/comment",params);
        ResultActions resultActions=mockMvcHelper(HttpMethod.POST,url,albumChatCommentReq,userLogin.getToken())
            .andExpect(status().isOk());

        MvcResult result = resultActions.andReturn();
        String content = result.getResponse().getContentAsString();
        AlbumChatCommentDto albumChatCommentDto = mapper.readValue(content, AlbumChatCommentDto.class);
        Long albumChatCommentId = albumChatCommentDto.getAlbumChatCommentId();

        params.put("albumChatCommentId",albumChatCommentId);
        url=urlGenerator.buildUrl("/api/album/{albumId}/comment/{albumChatCommentId}/commentLike",params);
        mockMvcHelper(HttpMethod.POST,url,albumChatCommentReq,userLogin.getToken())
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

        AlbumChatCommentReq albumChatCommentReq=AlbumChatCommentReq.createAlbumChatCommentReq(
                "안녕",null);
        params.clear();
        params.put("albumId",album.getAlbumId());
        String url=urlGenerator.buildUrl("/api/album/{albumId}/comment",params);
        mockMvcHelper(HttpMethod.POST,url,albumChatCommentReq,userLogin.getToken())
                .andExpect(status().isOk());
        params.put("albumChatCommentId",100L);
        url=urlGenerator.buildUrl("/api/album/{albumId}/comment/{albumChatCommentId}/commentLike",params);
        mockMvcHelper(HttpMethod.POST,url,albumChatCommentReq,userLogin.getToken())
                .andExpect(status().isNotFound());

    }
    @Test
    @Transactional
    @Sql("/data/albumChat.sql")
    public void commentLikeExistTest() throws Exception {
        User user=userRepository.findByEmail_Email("test1@example.com").get();
        user.setPassword(encoder.encode(user.getPassword()));
        UserLogin userLogin = loginUser("test1@example.com");

        Album album=albumRepository.findByTitleAndArtist_ArtistName("bam","bibi").get();

        AlbumChatCommentReq albumChatCommentReq=AlbumChatCommentReq.createAlbumChatCommentReq(
                "안녕",null);
        params.clear();
        params.put("albumId",album.getAlbumId());
        String url=urlGenerator.buildUrl("/api/album/{albumId}/comment",params);
        ResultActions resultActions=mockMvcHelper(HttpMethod.POST,url,albumChatCommentReq,userLogin.getToken())
                .andExpect(status().isOk());
        MvcResult result = resultActions.andReturn();
        String content = result.getResponse().getContentAsString();
        AlbumChatCommentDto albumChatCommentDto = mapper.readValue(content, AlbumChatCommentDto.class);
        Long albumChatCommentId = albumChatCommentDto.getAlbumChatCommentId();

        params.put("albumChatCommentId",albumChatCommentId);
        url=urlGenerator.buildUrl("/api/album/{albumId}/comment/{albumChatCommentId}/commentLike",params);
        mockMvcHelper(HttpMethod.POST,url,albumChatCommentReq,userLogin.getToken())
                .andExpect(status().isOk());
        mockMvcHelper(HttpMethod.POST,url,albumChatCommentReq,userLogin.getToken())
                .andExpect(status().isConflict());
    }
}
