package org.cosmic.backend.domainsTest.albumPost.comment;

import lombok.extern.log4j.Log4j2;
import org.cosmic.backend.domain.auth.applications.TokenProvider;
import org.cosmic.backend.domain.post.dtos.Comment.CreateCommentReq;
import org.cosmic.backend.domain.post.dtos.Post.PostDto;
import org.cosmic.backend.domain.post.entities.Post;
import org.cosmic.backend.domain.post.repositories.PostRepository;
import org.cosmic.backend.domain.user.domains.User;
import org.cosmic.backend.domain.user.repositorys.UsersRepository;
import org.cosmic.backend.domainsTest.BaseSetting;
import org.cosmic.backend.domainsTest.UrlGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Log4j2
@TestMethodOrder(MethodOrderer.MethodName.class)
@Transactional
@Sql("/data/albumPost.sql")
public class CreatePostCommentTest extends BaseSetting {
    @Autowired
    private UsersRepository userRepository;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private PostRepository postRepository;

    UrlGenerator urlGenerator=new UrlGenerator();
    Map<String,Object> params= new HashMap<>();
    private final String baseUrl = "/api/album/post/{postId}/comment/";

    private User user;
    private Post post;
    private String validToken;

    @BeforeEach
    public void setUp() {
        if(user == null){
            user = userRepository.findByEmail_Email("test1@example.com").orElseThrow();
        }
        if(post == null){
            post = postRepository.findByContent("밤양갱 노래좋다").orElseThrow();
        }
        validToken = tokenProvider.create(user);
    }

    @Test
    public void createCommentTest() throws Exception {
        CreateCommentReq createCommentReq = CreateCommentReq.builder()
                .userId(user.getUserId())
                .postId(post.getPostId())
                .content("안녕")
                .build();
        mockMvcHelper(HttpMethod.POST,"/api/comment/create",createCommentReq,validToken)
                .andExpect(status().isOk());
    }

    //post잘 만들어지는지
    @Test
    public void GivePostTest() throws Exception {
        PostDto postDto2=PostDto.builder()
                .postId(post.getPostId())
                .build();
        mockMvcHelper(HttpMethod.POST,"/api/comment/give",postDto2,validToken)
                .andExpect(status().isOk());
    }
}
