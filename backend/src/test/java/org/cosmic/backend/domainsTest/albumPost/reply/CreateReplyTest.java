package org.cosmic.backend.domainsTest.albumPost.reply;

import org.cosmic.backend.domain.post.dtos.Comment.CreateCommentRequest;
import org.cosmic.backend.domain.post.dtos.Reply.UpdateReplyReq;
import org.cosmic.backend.domain.post.entities.PostComment;
import org.cosmic.backend.domain.post.repositories.PostCommentRepository;
import org.cosmic.backend.domainsTest.albumPost.AlbumPostBaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.jdbc.Sql;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql(scripts = {"/data/albumPost.sql", "/data/CreateCommentAndLike.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
public class CreateReplyTest extends AlbumPostBaseTest {
    final String baseUrl = super.baseUrl + "/comment";

    @Autowired
    private PostCommentRepository postCommentRepository;

    private PostComment postComment;

    @BeforeEach
    @Override
    public void setUp(){
        super.setUp();
        if(postComment == null){
            postComment = postCommentRepository.findByPost_PostId(post.getPostId()).get(0);
        }
        params.put("commentId", 1);
    }

    @Test
    @Order(1)
    public void createReplyTest() throws Exception {
        CreateCommentRequest createReplyReq = CreateCommentRequest.builder()
                .parent_id(1L)
                .content("Hi")
                .build();
        mockMvcHelper(HttpMethod.POST,urlGenerator.buildUrl(baseUrl, params),createReplyReq,validToken)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Order(2)
    public void giveReplyTest() throws Exception {
        mockMvcHelper(HttpMethod.GET,urlGenerator.buildUrl(baseUrl, params),null,validToken)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Order(3)
    public void updateReplyTest() throws Exception {
        UpdateReplyReq updateReplyReq=UpdateReplyReq.builder()
                .content("안녕이라고 말 하지마")
                .build();
        params.replace("commentId", 2);
        mockMvcHelper(HttpMethod.POST,urlGenerator.buildUrl(baseUrl+"/{commentId}", params),updateReplyReq,validToken)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Order(4)
    public void deleteReplyTest() throws Exception {
        params.replace("commentId", 2);
        mockMvcHelper(HttpMethod.DELETE,urlGenerator.buildUrl(baseUrl+"/{commentId}", params),null,validToken)
                .andDo(print())
                .andExpect(status().isOk());
    }
}
