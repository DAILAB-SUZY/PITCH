package org.cosmic.backend.domains.albumChat.Comment;

import lombok.extern.log4j.Log4j2;
import org.cosmic.backend.domain.albumChat.domain.AlbumChat;
import org.cosmic.backend.domain.albumChat.dto.albumChat.AlbumChatResponse;
import org.cosmic.backend.domain.albumChat.dto.comment.AlbumChatCommentDto;
import org.cosmic.backend.domain.albumChat.dto.comment.CreateAlbumChatCommentReq;
import org.cosmic.backend.domain.albumChat.dto.commentlike.AlbumChatCommentLikeDto;
import org.cosmic.backend.domain.albumChat.dto.reply.CreateAlbumChatReplyReq;
import org.cosmic.backend.domain.albumChat.repository.AlbumChatRepository;
import org.cosmic.backend.domain.auth.dto.UserLogin;
import org.cosmic.backend.domain.playList.domain.Album;
import org.cosmic.backend.domain.playList.domain.Artist;
import org.cosmic.backend.domain.playList.repository.AlbumRepository;
import org.cosmic.backend.domain.playList.repository.ArtistRepository;
import org.cosmic.backend.domain.playList.repository.TrackRepository;
import org.cosmic.backend.domain.post.dto.Post.AlbumDto;
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
public class DeleteCommentTest {
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
    @Autowired
    AlbumChatRepository albumChatRepository;

    private ResultActions resultActions;
    private MvcResult result;
    @Test
    @Transactional
    public void deleteCommentTest() throws Exception {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        Email email = emailRepository.save(Email.builder()
                .email("testmand@example.com")
                .verificationCode("12345678")
                .verified(true)
                .build());

        User user=userRepository.save(User.builder()
                .email(email)
                .username("goodwill")
                .password(encoder.encode("12345678"))
                .build());

        UserLogin userLogin = UserLogin.builder()
                .email("testmand@example.com")
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

        AlbumChat albumChat= albumChatRepository.save(AlbumChat.builder()
                .CreateTime(now)
                .genre("발라드")
                .cover("base")
                .title("밤양갱")
                .album(album)
                .artistName("비비")
                .build()
        );

        resultActions=mockMvc.perform(MockMvcRequestBuilders.post("/api/albumchat/open")
                        .header("Authorization", "Bearer " + validToken)
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(AlbumDto.builder()
                                .albumId(album.getAlbumId())
                                .build()
                        )))
                .andDo(print())
                .andExpect(status().isOk());
        result = resultActions.andReturn();

        String content = result.getResponse().getContentAsString();
        AlbumChatResponse albumChatResponse = mapper.readValue(content, AlbumChatResponse.class); // 응답 JSON을 PostDto 객체로 변환
        Long albumChatId = albumChatResponse.getAlbumChatId();

        resultActions=mockMvc.perform(MockMvcRequestBuilders.post("/api/albumchat/comment/create")
                        .header("Authorization", "Bearer " + validToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(CreateAlbumChatCommentReq.builder()
                                .userId(user.getUserId())
                                .albumChatId(albumChatId)
                                .content("안녕")
                                .createTime(null)
                                .build()
                        )));
        result = resultActions.andReturn();

        content = result.getResponse().getContentAsString();
        AlbumChatCommentDto albumChatCommentDto = mapper.readValue(content, AlbumChatCommentDto.class); // 응답 JSON을 PostDto 객체로 변환
        //Long albumChatCommentId = albumChatCommentDto.getAlbumChatCommentId();

        mockMvc.perform(post("/api/albumchat/comment/delete")
                        .header("Authorization", "Bearer " + validToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(albumChatCommentDto))).andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    @Transactional
    public void DeleteNotMatchCommentTest() throws Exception {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        Email email = emailRepository.save(Email.builder()
                .email("testmand@example.com")
                .verificationCode("12345678")
                .verified(true)
                .build());

        User user=userRepository.save(User.builder()
                .email(email)
                .username("goodwill")
                .password(encoder.encode("12345678"))
                .build());

        UserLogin userLogin = UserLogin.builder()
                .email("testmand@example.com")
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

        AlbumChat albumChat= albumChatRepository.save(AlbumChat.builder()
                .CreateTime(now)
                .genre("발라드")
                .cover("base")
                .title("밤양갱")
                .album(album)
                .artistName("비비")
                .build()
        );

        resultActions=mockMvc.perform(MockMvcRequestBuilders.post("/api/albumchat/open")
                        .header("Authorization", "Bearer " + validToken)
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(AlbumDto.builder()
                                .albumId(album.getAlbumId())
                                .build()
                        )))
                .andDo(print())
                .andExpect(status().isOk());
        result = resultActions.andReturn();

        String content = result.getResponse().getContentAsString();
        AlbumChatResponse albumChatResponse = mapper.readValue(content, AlbumChatResponse.class); // 응답 JSON을 PostDto 객체로 변환
        Long albumChatId = albumChatResponse.getAlbumChatId();

        resultActions=mockMvc.perform(MockMvcRequestBuilders.post("/api/albumchat/comment/create")
                .header("Authorization", "Bearer " + validToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(CreateAlbumChatCommentReq.builder()
                        .userId(user.getUserId())
                        .albumChatId(albumChatId)
                        .content("안녕")
                        .createTime(null)
                        .build()
                )));
        result = resultActions.andReturn();

        content = result.getResponse().getContentAsString();
        AlbumChatCommentDto albumChatCommentDto = mapper.readValue(content, AlbumChatCommentDto.class); // 응답 JSON을 PostDto 객체로 변환

        albumChatCommentDto.setAlbumChatCommentId(100L);

        mockMvc.perform(post("/api/albumchat/comment/delete")
                        .header("Authorization", "Bearer " + validToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(albumChatCommentDto))).andDo(print())
                .andExpect(status().isNotFound());
    }
    @Test
    @Transactional
    public void deleteCommentLikeAndReplyTest() throws Exception {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        Email email = emailRepository.save(Email.builder()
                .email("testmand@example.com")
                .verificationCode("12345678")
                .verified(true)
                .build());

        User user=userRepository.save(User.builder()
                .email(email)
                .username("goodwill")
                .password(encoder.encode("12345678"))
                .build());

        UserLogin userLogin = UserLogin.builder()
                .email("testmand@example.com")
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

        AlbumChat albumChat= albumChatRepository.save(AlbumChat.builder()
                .CreateTime(now)
                .genre("발라드")
                .cover("base")
                .title("밤양갱")
                .album(album)
                .artistName("비비")
                .build()
        );

        resultActions=mockMvc.perform(MockMvcRequestBuilders.post("/api/albumchat/open")
                        .header("Authorization", "Bearer " + validToken)
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(AlbumDto.builder()
                                .albumId(album.getAlbumId())
                                .build()
                        )))
                .andDo(print())
                .andExpect(status().isOk());
        result = resultActions.andReturn();

        String content = result.getResponse().getContentAsString();
        AlbumChatResponse albumChatResponse = mapper.readValue(content, AlbumChatResponse.class); // 응답 JSON을 PostDto 객체로 변환
        Long albumChatId = albumChatResponse.getAlbumChatId();

        resultActions=mockMvc.perform(MockMvcRequestBuilders.post("/api/albumchat/comment/create")
                .header("Authorization", "Bearer " + validToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(CreateAlbumChatCommentReq.builder()
                        .userId(user.getUserId())
                        .albumChatId(albumChatId)
                        .content("안녕")
                        .createTime(null)
                        .build()
                )));
        result = resultActions.andReturn();

        content = result.getResponse().getContentAsString();
        AlbumChatCommentDto albumChatCommentDto = mapper.readValue(content, AlbumChatCommentDto.class); // 응답 JSON을 PostDto 객체로 변환
        Long albumChatCommentId = albumChatCommentDto.getAlbumChatCommentId();

        mockMvc.perform(post("/api/albumchat/reply/create")
                        .header("Authorization", "Bearer " + validToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(CreateAlbumChatReplyReq.builder()
                                .albumChatCommentId(albumChatCommentId)
                                .content("hello")
                                .createTime(null)
                                .userId(user.getUserId())
                                .build()
                        )));
        mockMvc.perform(post("/api/albumchat/commentlike/create")
                        .header("Authorization", "Bearer " + validToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(AlbumChatCommentLikeDto.builder()
                                .albumChatCommentId(albumChatCommentId)
                                .userId(user.getUserId())
                                .build()
                        )));

        mockMvc.perform(post("/api/albumchat/comment/delete")
                        .header("Authorization", "Bearer " + validToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(albumChatCommentDto)));

        mockMvc.perform(post("/api/albumchat/reply/give")
                        .header("Authorization", "Bearer " + validToken)
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(AlbumChatCommentDto.builder()
                                .albumChatCommentId(albumChatCommentId)
                                .build()
                        )))
                .andDo(print())
                .andExpect(status().isNotFound());

        mockMvc.perform(post("/api/albumchat/commentlike/give")
                        .header("Authorization", "Bearer " + validToken)
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(AlbumChatCommentDto.builder()
                                .albumChatCommentId(albumChatCommentId)
                                .build()
                        )))
                .andDo(print())
                .andExpect(status().isNotFound());
    }


}
