package org.cosmic.backend.domainsTest.albumPost.comment;

import org.cosmic.backend.domain.post.dtos.Comment.CreateCommentReq;
import org.cosmic.backend.domain.post.dtos.Comment.UpdateCommentReq;
import org.cosmic.backend.domainsTest.albumPost.AlbumPostBaseTest;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PostCommentTest extends AlbumPostBaseTest {
    private final String baseUrl = super.baseUrl + "/comment/";

    @Test
    @Order(1)
    public void createCommentTest() throws Exception {
        CreateCommentReq createCommentReq = CreateCommentReq.builder()
                .content("안녕")
                .build();
        mockMvcHelper(HttpMethod.POST,urlGenerator.buildUrl(baseUrl, params),createCommentReq,validToken)
                .andDo(print())
                .andExpect(status().isOk());
    }

    //post잘 만들어지는지
    @Test
    @Order(2)
    public void GivePostTest() throws Exception {
        mockMvcHelper(HttpMethod.GET,urlGenerator.buildUrl(baseUrl, params),null, validToken)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Order(3)
    public void updateCommentTest() throws Exception {
        UpdateCommentReq updateCommentReq=UpdateCommentReq.builder()
                .content("밤양갱 노래 별론대")
                .build();
        params.put("commentId", post.getPostComments().get(0).getCommentId());
        mockMvcHelper(HttpMethod.POST,urlGenerator.buildUrl(baseUrl+"{commentId}", params),updateCommentReq,validToken);
    }

    @Test
    @Order(4)
    public void deleteCommentTest() throws Exception {
        params.put("commentId", post.getPostComments().get(0).getCommentId());
        mockMvcHelper(HttpMethod.DELETE,urlGenerator.buildUrl(baseUrl+"{commentId}", params),null,validToken)
                .andExpect(status().isOk());
    }

}
