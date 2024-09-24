package org.cosmic.backend.domainsTest.albumChat.commentLike;

import lombok.extern.log4j.Log4j2;
import org.cosmic.backend.domainsTest.albumChat.AlbumChatBaseTest;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.transaction.annotation.Transactional;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Log4j2
public class CreatePostCommentLikeTest extends AlbumChatBaseTest {
    private final String baseUrl = super.baseUrl + "/comment";
    @Test
    @Order(1)
    public void commentLikeCreateTest() throws Exception {
        //2명이상 일 때
        params.put("albumChatCommentId",albumChatComment.getAlbumChatCommentId());
        mockMvcHelper(HttpMethod.POST,urlGenerator.buildUrl(baseUrl+"/{albumChatCommentId}/commentLike",params),null,validToken)
            .andExpect(status().isOk());
    }
    @Test
    @Order(2)
    public void commentLikesGiveTest() throws Exception {
        params.put("albumChatCommentId",albumChatComment.getAlbumChatCommentId());
        mockMvcHelper(HttpMethod.GET,urlGenerator.buildUrl(baseUrl+"/{albumChatCommentId}/commentLike",params),null,validToken)
                .andExpect(status().isOk());
    }

    @Test
    @Order(3)
    public void commentLikeExistTest() throws Exception {
        params.put("albumChatCommentId",albumChatComment.getAlbumChatCommentId());
        mockMvcHelper(HttpMethod.POST,urlGenerator.buildUrl(baseUrl+"/{albumChatCommentId}/commentLike",params),null,validToken)
                .andExpect(status().isConflict());
    }

    @Test
    @Transactional
    @Order(4)
    public void commentLikeDeleteTest() throws Exception {
        params.put("albumChatCommentId",albumChatComment.getAlbumChatCommentId());
        mockMvcHelper(HttpMethod.DELETE,urlGenerator.buildUrl(baseUrl+"/{albumChatCommentId}/commentLike",params),null,validToken)
                .andExpect(status().isOk());
    }
}
