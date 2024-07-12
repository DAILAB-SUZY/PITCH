package org.cosmic.backend.domains.albumPost.Post;

import lombok.extern.log4j.Log4j2;
import org.cosmic.backend.domain.auth.dto.UserLogin;
import org.cosmic.backend.domain.playList.domain.Album;
import org.cosmic.backend.domain.playList.domain.Artist;
import org.cosmic.backend.domain.playList.domain.Track;
import org.cosmic.backend.domain.playList.repository.AlbumRepository;
import org.cosmic.backend.domain.playList.repository.ArtistRepository;
import org.cosmic.backend.domain.playList.repository.TrackRepository;
import org.cosmic.backend.domain.post.dto.Post.CreatePost;
import org.cosmic.backend.domain.post.dto.Post.PostDto;
import org.cosmic.backend.domain.post.dto.Post.UpdatePost;
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
public class UpdatePostTest {
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
    //내용 잘 바뀌는지

    @Test
    @Transactional
    public void UpdatepostTest() throws Exception {

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        Email email = emailRepository.save(Email.builder()
                .email("testgirl@example.com")
                .verificationCode("12345678")
                .verified(true)
                .build());

        User user=userRepository.save(User.builder()
                .email(email)
                .username("goodwill")
                .password(encoder.encode("12345678"))
                .build());

        UserLogin userLogin = UserLogin.builder()
                .email("testgirl@example.com")
                .password("12345678")
                .build();

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(userLogin)));

        MvcResult result = resultActions.andReturn();
        String validToken = mapper.readValue(result.getResponse().getContentAsString(), UserLogin.class).getToken();

        Instant now = Instant.now();

        Artist artist=artistRepository.save(Artist.builder()
                .artistName("비비")
                .build());
        Album album=albumRepository.save(Album.builder()
                .title("밤양갱")
                .cover("base")
                .artist(artist)
                .createdDate(now)
                .genre("발라드")
                .build());

        Track track=trackRepository.save(Track.builder()
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

        mockMvc.perform(post("/api/post/update")
                        .header("Authorization", "Bearer " + validToken)
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(UpdatePost.builder()
                                .updateTime(null)
                                .content("밤양갱 노래 별론대")
                                .postId(postId)
                                .build()
                        )))
                .andDo(print())
                .andExpect(status().isOk());
    }

}
