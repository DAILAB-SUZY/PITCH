package org.cosmic.backend.domainsTest.albumChat.albumChat;

import lombok.extern.log4j.Log4j2;
import org.cosmic.backend.domain.albumChat.dtos.comment.AlbumChatCommentDto;
import org.cosmic.backend.domain.albumChat.dtos.comment.AlbumChatCommentReq;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.*;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Log4j2
public class ManyLikeAlbumChatPostCommentTest extends BaseSetting {
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
    UrlGenerator urlGenerator=new UrlGenerator();
    Map<String,Object> params= new HashMap<>();

    @Test
    @Transactional
    @Sql("/data/albumChat.sql")
    public void manyLikeAlbumChatCommentTest() throws Exception {
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

        params.clear();
        params.put("albumId",album.getAlbumId());
        String url=urlGenerator.buildUrl("/api/album/{albumId}/comment",params);
        ResultActions resultActions=mockMvcHelper(HttpMethod.POST,url,albumChatCommentReq,userLogin.getToken())
            .andExpect(status().isOk());

        MvcResult result = resultActions.andReturn();
        String content = result.getResponse().getContentAsString();
        AlbumChatCommentDto albumChatCommentDto2 = mapper.readValue(content, AlbumChatCommentDto.class);
        Long albumChatCommentId1 = albumChatCommentDto2.getAlbumChatCommentId();

        params.clear();
        params.put("albumId",album.getAlbumId());
        params.put("albumChatCommentId",albumChatCommentId1);
        url=urlGenerator.buildUrl("/api/album/{albumId}/comment/{albumChatCommentId}/commentLike",params);
        mockMvcHelper(HttpMethod.POST,url,null,userLogin.getToken())
                .andExpect(status().isOk());

        //---

        params.clear();
        params.put("albumId",album.getAlbumId());
        url=urlGenerator.buildUrl("/api/album/{albumId}/comment",params);
        resultActions=mockMvcHelper(HttpMethod.POST,url,albumChatCommentReq,userLogin.getToken())
                .andExpect(status().isOk());
        result = resultActions.andReturn();
        content = result.getResponse().getContentAsString();
        AlbumChatCommentDto albumChatCommentDto = mapper.readValue(content, AlbumChatCommentDto.class);
        Long albumChatCommentId2 = albumChatCommentDto.getAlbumChatCommentId();


        params.clear();
        params.put("albumId",album.getAlbumId());
        params.put("albumChatCommentId",albumChatCommentId2);
        url=urlGenerator.buildUrl("/api/album/{albumId}/comment/{albumChatCommentId}/commentLike",params);
        mockMvcHelper(HttpMethod.POST,url,null,userLogin.getToken())//user1
                .andExpect(status().isOk());

        params.clear();
        params.put("albumId",album.getAlbumId());
        params.put("albumChatCommentId",albumChatCommentId2);
        url=urlGenerator.buildUrl("/api/album/{albumId}/comment/{albumChatCommentId}/commentLike",params);
        mockMvcHelper(HttpMethod.POST,url,null,userLogin2.getToken())//user2
                .andExpect(status().isOk());


        params.clear();
        params.put("albumId",album.getAlbumId());
        url=urlGenerator.buildUrl("/api/album/{albumId}?sorted=manylike",params);
        System.out.println("*******"+url);
        mockMvcHelper(HttpMethod.GET,url,null,userLogin.getToken())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].albumChatCommentId", is(2)))
                .andExpect(jsonPath("$[1].albumChatCommentId", is(1)));

        }
    }

