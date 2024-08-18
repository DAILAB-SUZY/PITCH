package org.cosmic.backend.domains.albumPost.reply;

import lombok.extern.log4j.Log4j2;
import org.cosmic.backend.domain.auth.dtos.UserLogin;
import org.cosmic.backend.domain.playList.domains.Album;
import org.cosmic.backend.domain.playList.domains.Artist;
import org.cosmic.backend.domain.playList.domains.Track;
import org.cosmic.backend.domain.playList.repositorys.AlbumRepository;
import org.cosmic.backend.domain.playList.repositorys.ArtistRepository;
import org.cosmic.backend.domain.playList.repositorys.TrackRepository;
import org.cosmic.backend.domain.post.dtos.Comment.CommentDto;
import org.cosmic.backend.domain.post.dtos.Comment.CreateCommentReq;
import org.cosmic.backend.domain.post.dtos.Post.CreatePost;
import org.cosmic.backend.domain.post.dtos.Post.PostDto;
import org.cosmic.backend.domain.post.dtos.Reply.CreateReplyReq;
import org.cosmic.backend.domain.post.dtos.Reply.ReplyDto;
import org.cosmic.backend.domain.user.domains.User;
import org.cosmic.backend.domain.user.repositorys.EmailRepository;
import org.cosmic.backend.domain.user.repositorys.UsersRepository;
import org.cosmic.backend.domains.BaseSetting;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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
public class DeleteReplyTest extends BaseSetting {
    @Autowired
    private MockMvc mockMvc;
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

    @Test
    @Transactional
    public void deleteReplyTest() throws Exception {
        UserLogin userLogin = loginUser("test@example.com");
        String validToken=userLogin.getToken();
        User user=getUser();
        Instant now = Instant.now();

        Artist artist=saveArtist("비비");

        Album album=saveAlbum("밤양갱", artist, now);
        Track track=saveTrack(album,artist,now);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/post/create")
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

        MvcResult result = resultActions.andReturn();

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
