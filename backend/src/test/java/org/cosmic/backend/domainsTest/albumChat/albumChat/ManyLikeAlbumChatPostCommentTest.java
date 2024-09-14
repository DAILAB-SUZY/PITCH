package org.cosmic.backend.domainsTest.albumChat.albumChat;

import lombok.extern.log4j.Log4j2;
import org.cosmic.backend.domain.albumChat.dtos.comment.AlbumChatCommentDetail;
import org.cosmic.backend.domain.albumChat.dtos.comment.AlbumChatCommentRequest;
import org.cosmic.backend.domain.auth.dtos.UserLoginDetail;
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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.shaded.com.fasterxml.jackson.core.type.TypeReference;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Log4j2
public class ManyLikeAlbumChatPostCommentTest extends BaseSetting {
    ObjectMapper objectMapper = new ObjectMapper();
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
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Test
    @Transactional
    @Sql("/data/albumChat.sql")
        public void manyLikeAlbumChatCommentTest() throws Exception {
            User user=userRepository.findByEmail_Email("test1@example.com").get();
            user.setPassword(encoder.encode(user.getPassword()));
            UserLoginDetail userLogin = loginUser("test1@example.com");

            User user2=userRepository.findByEmail_Email("test2@example.com").get();
            user2.setPassword(encoder.encode(user2.getPassword()));
            UserLoginDetail userLogin2 = loginUser("test2@example.com");

            Album album=albumRepository.findByTitleAndArtist_ArtistName("bam","bibi").get();
            AlbumChatCommentRequest albumChatCommentReq=AlbumChatCommentRequest.createAlbumChatCommentReq(
                "안녕",null);
            params.clear();
            params.put("albumId",album.getAlbumId());
            String url=urlGenerator.buildUrl("/api/album/{albumId}/comment",params);
            ResultActions resultActions=mockMvcHelper(HttpMethod.POST,url,albumChatCommentReq,userLogin.getToken())
                .andExpect(status().isOk());
            String jsonResponse=resultActions.andReturn().getResponse().getContentAsString();
            List<AlbumChatCommentDetail> albumChatCommentDetails =
                objectMapper.readValue(jsonResponse, new TypeReference<List<AlbumChatCommentDetail>>() {});
            Long albumChatCommentId=albumChatCommentDetails.get(0).getAlbumChatCommentId();

            params.clear();
            params.put("albumId",album.getAlbumId());
            params.put("albumChatCommentId",albumChatCommentId);
            url=urlGenerator.buildUrl("/api/album/{albumId}/comment/{albumChatCommentId}/commentLike",params);
            mockMvcHelper(HttpMethod.POST,url,null,userLogin.getToken())
                    .andExpect(status().isOk());

            //---

            params.clear();
            params.put("albumId",album.getAlbumId());
            url=urlGenerator.buildUrl("/api/album/{albumId}/comment",params);
            resultActions=mockMvcHelper(HttpMethod.POST,url,albumChatCommentReq,userLogin.getToken())
                .andExpect(status().isOk());
            jsonResponse=resultActions.andReturn().getResponse().getContentAsString();
            albumChatCommentDetails = objectMapper.readValue(jsonResponse, new TypeReference<List<AlbumChatCommentDetail>>() {});
            Long albumChatCommentId2=albumChatCommentDetails.get(1).getAlbumChatCommentId();


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
            resultActions=mockMvcHelper(HttpMethod.GET,url,null,userLogin.getToken());
            jsonResponse=resultActions.andReturn().getResponse().getContentAsString();
            albumChatCommentDetails = objectMapper.readValue(jsonResponse, new TypeReference<List<AlbumChatCommentDetail>>() {});


        }
    }

