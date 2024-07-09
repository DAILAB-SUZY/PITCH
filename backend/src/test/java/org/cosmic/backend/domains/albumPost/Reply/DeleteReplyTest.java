package org.cosmic.backend.domains.albumPost.Reply;

import lombok.extern.log4j.Log4j2;
import org.cosmic.backend.domain.auth.dto.UserLogin;
import org.cosmic.backend.domain.playList.domain.Album;
import org.cosmic.backend.domain.playList.domain.Artist;
import org.cosmic.backend.domain.playList.domain.Track;
import org.cosmic.backend.domain.playList.repository.AlbumRepository;
import org.cosmic.backend.domain.playList.repository.ArtistRepository;
import org.cosmic.backend.domain.playList.repository.TrackRepository;
import org.cosmic.backend.domain.post.dto.Comment.CommentDto;
import org.cosmic.backend.domain.post.dto.Comment.CreateCommentReq;
import org.cosmic.backend.domain.post.dto.Post.CreatePost;
import org.cosmic.backend.domain.post.dto.Post.PostDto;
import org.cosmic.backend.domain.post.dto.Reply.CreateReplyReq;
import org.cosmic.backend.domain.post.dto.Reply.ReplyDto;
import org.cosmic.backend.domain.post.dto.Reply.UpdateReplyReq;
import org.cosmic.backend.domain.user.domain.Email;
import org.cosmic.backend.domain.user.domain.User;
import org.cosmic.backend.domain.user.repository.EmailRepository;
import org.cosmic.backend.domain.user.repository.UsersRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.time.Instant;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Log4j2
public class DeleteReplyTest {
    @Autowired
    private MockMvc mockMvc;
    ObjectMapper mapper = new ObjectMapper();
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

    private ResultActions resultActions;
    private MvcResult result;
    @Test
    @Transactional
    public void DeleteReplyTest() throws Exception {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        Email email = emailRepository.save(Email.builder()
                .email("testma@example.com")
                .verificationCode("12345678")
                .verified(true)
                .build());

        User user = userRepository.save(User.builder()
                .email(email)
                .username("goodwill")
                .password(encoder.encode("12345678"))
                .build());

        UserLogin userLogin = UserLogin.builder()
                .email("testma@example.com")
                .password("12345678")
                .build();

        resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(userLogin)));

        result = resultActions.andReturn();
        String validToken = mapper.readValue(result.getResponse().getContentAsString(), UserLogin.class).getToken();

        Instant now = Instant.now();

        Artist artist = artistRepository.save(Artist.builder()
                .artistName("비비")
                .build());
        Album album = albumRepository.save(Album.builder()
                .title("밤양갱")
                .cover("base")
                .artist(artist)
                .createdDate(now)
                .genre("발라드")
                .build());

        Track track = trackRepository.save(Track.builder()
                .title("밤양갱")
                .album(album)
                .Cover("base")
                .artist(artist)
                .createdDate(now)
                .genre("발라드")
                .build());

        resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/post/create")
                .header("Authorization", "Bearer " + validToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(CreatePost.builder()
                        .userId(user.getUserId())
                        .cover("base")
                        .artistName("비비")
                        .content("밤양갱 노래좋다")
                        .title("밤양갱")
                        .updateTime(null)
                        .build()
                )));

        result = resultActions.andReturn();

        String content = result.getResponse().getContentAsString();
        PostDto postDto = mapper.readValue(content, PostDto.class); // 응답 JSON을 PostDto 객체로 변환
        Long postId = postDto.getPostId();

        resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/comment/create")
                .header("Authorization", "Bearer " + validToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(CreateCommentReq.builder()
                        .userId(user.getUserId())
                        .createTime(null)
                        .postId(postId)
                        .content("안녕")
                        .build()
                )));

        result = resultActions.andReturn();
        content = result.getResponse().getContentAsString();
        CommentDto comment = mapper.readValue(content, CommentDto.class); // 응답 JSON을 PostDto 객체로 변환
        Long commentId = comment.getCommentId();

        resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/reply/create")
                .header("Authorization", "Bearer " + validToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(CreateReplyReq.builder()
                        .userId(user.getUserId())
                        .createTime(null)
                        .commentId(commentId)
                        .content("안녕")
                        .build()
                )));

        result = resultActions.andReturn();
        content = result.getResponse().getContentAsString();
        ReplyDto reply = mapper.readValue(content, ReplyDto.class); // 응답 JSON을 PostDto 객체로 변환
        Long replyId = reply.getReplyId();

        mockMvc.perform(post("/api/reply/delete")
                        .header("Authorization", "Bearer " + validToken)
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(ReplyDto.builder()
                                .replyId(replyId)
                                .build()
                        )))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
