package org.cosmic.backend.domainsTest.albumPost.post;

import lombok.extern.log4j.Log4j2;
import org.cosmic.backend.domain.auth.dtos.UserLogin;
import org.cosmic.backend.domain.playList.repositorys.AlbumRepository;
import org.cosmic.backend.domain.playList.repositorys.ArtistRepository;
import org.cosmic.backend.domain.playList.repositorys.TrackRepository;
import org.cosmic.backend.domain.post.dtos.Comment.CreateCommentReq;
import org.cosmic.backend.domain.post.dtos.Like.LikeDto;
import org.cosmic.backend.domain.post.dtos.Post.CreatePost;
import org.cosmic.backend.domain.post.dtos.Post.PostDto;
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

import java.net.http.HttpClient;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Log4j2
public class DeletePostTest extends BaseSetting {
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
    //삭제하면 다 사라지는지
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private MvcResult result;
    UrlGenerator urlGenerator=new UrlGenerator();
    Map<String,Object> params= new HashMap<>();

    @Test
    @Transactional
    @Sql("/data/albumPost.sql")
    public void deletePostTest() throws Exception {
        User user=userRepository.findByEmail_Email("test1@example.com").get();
        user.setPassword(encoder.encode(user.getPassword()));
        UserLogin userLogin = loginUser("test1@example.com");

        CreatePost createPost=CreatePost.createCreatePost
            (user.getUserId(),"base","bibi","밤양갱 노래좋다","bam",null);
        String url="/api/post/create";
        ResultActions resultActions =mockMvcHelper(HttpMethod.POST,url,createPost,userLogin.getToken());
        result = resultActions.andReturn();
        String content = result.getResponse().getContentAsString();
        PostDto postDto = mapper.readValue(content, PostDto.class);
        Long postId = postDto.getPostId();

        PostDto postDto1=PostDto.createPostDto(postId);
        url="/api/post/delete";
        mockMvcHelper(HttpMethod.POST,url,postDto1,userLogin.getToken()).andExpect(status().isOk());
    }

    @Test
    @Transactional
    @Sql("/data/albumPost.sql")
    public void deletePostCommentTest() throws Exception {
        User user=userRepository.findByEmail_Email("test1@example.com").get();
        user.setPassword(encoder.encode(user.getPassword()));
        UserLogin userLogin = loginUser("test1@example.com");

        CreatePost createPost=CreatePost.createCreatePost
                (user.getUserId(),"base","bibi","밤양갱 노래좋다","bam",null);
        String url="/api/post/create";
        ResultActions resultActions =mockMvcHelper(HttpMethod.POST,url,createPost,userLogin.getToken());
        result = resultActions.andReturn();
        String content = result.getResponse().getContentAsString();
        PostDto postDto = mapper.readValue(content, PostDto.class);
        Long postId = postDto.getPostId();

        CreateCommentReq createCommentReq=CreateCommentReq.createCreateCommentReq(user.getUserId(),null,postId,"안녕");
        url="/api/comment/create";
        mockMvcHelper(HttpMethod.POST,url,createCommentReq,userLogin.getToken());

        PostDto postDto1=PostDto.createPostDto(postId);
        url="/api/post/delete";
        mockMvcHelper(HttpMethod.POST,url,postDto1,userLogin.getToken()).andExpect(status().isOk());
        url="/api/comment/give";
        mockMvcHelper(HttpMethod.POST,url,postDto1,userLogin.getToken()).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    @Sql("/data/albumPost.sql")
    public void deletePostLikeTest() throws Exception {
        User user=userRepository.findByEmail_Email("test1@example.com").get();
        user.setPassword(encoder.encode(user.getPassword()));
        UserLogin userLogin = loginUser("test1@example.com");

        CreatePost createPost=CreatePost.createCreatePost
                (user.getUserId(),"base","bibi","밤양갱 노래좋다","bam",null);
        String url="/api/post/create";
        ResultActions resultActions =mockMvcHelper(HttpMethod.POST,url,createPost,userLogin.getToken());
        result = resultActions.andReturn();
        String content = result.getResponse().getContentAsString();
        PostDto postDto = mapper.readValue(content, PostDto.class);
        Long postId = postDto.getPostId();

        LikeDto likeDto=LikeDto.createLikeDto(user.getUserId(),postId);
        url="/api/like/create";
        mockMvcHelper(HttpMethod.POST,url,likeDto,userLogin.getToken());

        PostDto postDto1=PostDto.createPostDto(postId);
        url="/api/post/delete";
        mockMvcHelper(HttpMethod.POST,url,postDto1,userLogin.getToken()).andExpect(status().isOk());
        url="/api/like/give";
        mockMvcHelper(HttpMethod.POST,url,postDto1,userLogin.getToken()).andExpect(status().isNotFound());
    }
}
