package org.cosmic.backend.domainsTest.albumChat.comment;

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
import org.cosmic.backend.domainsTest.albumChat.AlbumChatBaseTest;
import org.junit.jupiter.api.Order;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Log4j2
public class CreatePostCommentTest extends AlbumChatBaseTest {
    private final String baseUrl = super.baseUrl + "/comment";

    @Test
    @Order(1)
    public void commentCreateManyLikeTest() throws Exception {
        params.put("albumChatCommentId",albumChatComment.getAlbumChatCommentId());
        mockMvcHelper(HttpMethod.POST,urlGenerator.buildUrl("/api/album/{albumId}/comment/{albumChatCommentId}/commentLike",params),null,validToken)
                .andExpect(status().isOk());

        //---

        params.replace("albumChatCommentId",albumChatComment2.getAlbumChatCommentId());
        mockMvcHelper(HttpMethod.POST,urlGenerator.buildUrl("/api/album/{albumId}/comment/{albumChatCommentId}/commentLike",params),null,validToken)//user1
                .andExpect(status().isOk());

        mockMvcHelper(HttpMethod.POST,urlGenerator.buildUrl("/api/album/{albumId}/comment/{albumChatCommentId}/commentLike",params),null,validToken2)//user2
                .andExpect(status().isOk());

        AlbumChatCommentRequest albumChatCommentReq=AlbumChatCommentRequest.createAlbumChatCommentReq(
                "안녕",null,"manylike",0,null);
        mockMvcHelper(HttpMethod.POST,urlGenerator.buildUrl(baseUrl,params),albumChatCommentReq,validToken)
                .andExpect(status().isOk());
        assertEquals(2L, albumChatComment2.getAlbumChatCommentId());
        assertEquals(1L, albumChatComment.getAlbumChatCommentId());
    }
    @Test
    @Order(2)
    public void commentCreateRecentTest() throws Exception {
        AlbumChatCommentRequest albumChatCommentReq=AlbumChatCommentRequest.createAlbumChatCommentReq(
                "안녕",null,"recent",0,null);
        mockMvcHelper(HttpMethod.POST,urlGenerator.buildUrl(baseUrl,params),albumChatCommentReq,validToken)
                .andExpect(status().isOk());

        assertEquals(1L, albumChatComment.getAlbumChatCommentId());
        assertEquals(2L, albumChatComment2.getAlbumChatCommentId());
    }

    @Test
    @Order(3)
    public void commentUpdateTest() throws Exception {
        AlbumChatCommentRequest albumChatCommentReq=AlbumChatCommentRequest.createAlbumChatCommentReq(
            "hi",null,"manylike",0,null);
        params.put("albumChatCommentId",albumChatComment.getAlbumChatCommentId());
        mockMvcHelper(HttpMethod.POST,urlGenerator.buildUrl(baseUrl+"/{albumChatCommentId}",params),albumChatCommentReq,validToken)
                .andExpect(status().isOk());
    }

    @Test
    @Order(4)
    public void commentDeleteTest() throws Exception {
        params.put("albumChatCommentId",albumChatComment.getAlbumChatCommentId());
        mockMvcHelper(HttpMethod.DELETE,urlGenerator.buildUrl(baseUrl+"/{albumChatCommentId}?sorted=manylike&count=0",params),null,validToken)
                .andExpect(status().isOk());
    }
}
