package org.cosmic.backend.domainsTest.albumPost.reply;

import lombok.extern.log4j.Log4j2;
import org.cosmic.backend.domain.auth.dtos.UserLogin;
import org.cosmic.backend.domain.playList.repositorys.AlbumRepository;
import org.cosmic.backend.domain.playList.repositorys.ArtistRepository;
import org.cosmic.backend.domain.playList.repositorys.TrackRepository;
import org.cosmic.backend.domain.post.dtos.Comment.CommentDto;
import org.cosmic.backend.domain.post.dtos.Comment.CreateCommentReq;
import org.cosmic.backend.domain.post.dtos.Post.CreatePost;
import org.cosmic.backend.domain.post.dtos.Post.PostDto;
import org.cosmic.backend.domain.post.dtos.Reply.CreateReplyReq;
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
public class CreateReplyTest extends BaseSetting {
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
    private MvcResult result;

    @Test
    @Transactional
    @Sql("/data/albumPost.sql")
    public void createReplyTest() throws Exception {
        User user=userRepository.findByEmail_Email("test1@example.com").get();
        user.setPassword(encoder.encode(user.getPassword()));
        UserLogin userLogin = loginUser("test1@example.com");

        CreatePost createPost=CreatePost.createCreatePost
                (user.getUserId(),"base","bibi","밤양갱 노래좋다","bam",null);
        ResultActions resultActions =mockMvcHelper("/api/post/create",createPost);
        result = resultActions.andReturn();
        String content = result.getResponse().getContentAsString();
        PostDto postDto = mapper.readValue(content, PostDto.class); // 응답 JSON을 PostDto 객체로 변환
        Long postId = postDto.getPostId();

        CreateCommentReq createCommentReq=CreateCommentReq.createCreateCommentReq
                (user.getUserId(),null,postId,"안녕");
        resultActions=mockMvcHelper("/api/comment/create",createCommentReq).andExpect(status().isOk());
        result = resultActions.andReturn();
        content = result.getResponse().getContentAsString();
        CommentDto comment = mapper.readValue(content, CommentDto.class); // 응답 JSON을 PostDto 객체로 변환
        Long commentId = comment.getCommentId();

        CreateReplyReq createReplyReq =CreateReplyReq.createCreateReplyReq
            (user.getUserId(),commentId,"안녕",null);
        mockMvcHelper("/api/reply/create",createReplyReq).andExpect(status().isOk());
    }

    //post잘 만들어지는지
    @Test
    @Transactional
    @Sql("/data/albumPost.sql")
    public void giveReplyTest() throws Exception {
        User user=userRepository.findByEmail_Email("test1@example.com").get();
        user.setPassword(encoder.encode(user.getPassword()));
        UserLogin userLogin = loginUser("test1@example.com");

        CreatePost createPost=CreatePost.createCreatePost
                (user.getUserId(),"base","bibi","밤양갱 노래좋다","bam",null);
        ResultActions resultActions =mockMvcHelper("/api/post/create",createPost);
        result = resultActions.andReturn();
        String content = result.getResponse().getContentAsString();
        PostDto postDto = mapper.readValue(content, PostDto.class); // 응답 JSON을 PostDto 객체로 변환
        Long postId = postDto.getPostId();

        CreateCommentReq createCommentReq=CreateCommentReq.createCreateCommentReq
                (user.getUserId(),null,postId,"안녕");
        resultActions=mockMvcHelper("/api/comment/create",createCommentReq).andExpect(status().isOk());
        result = resultActions.andReturn();
        content = result.getResponse().getContentAsString();
        CommentDto comment = mapper.readValue(content, CommentDto.class); // 응답 JSON을 PostDto 객체로 변환
        Long commentId = comment.getCommentId();

        CreateReplyReq createReplyReq =CreateReplyReq.createCreateReplyReq
            (user.getUserId(),commentId,"안녕",null);
        mockMvcHelper("/api/reply/create",createReplyReq).andExpect(status().isOk());
        CommentDto commentDto= CommentDto.createCommentDto(commentId);
        mockMvcHelper("/api/reply/give",commentDto).andExpect(status().isOk());
    }
}
