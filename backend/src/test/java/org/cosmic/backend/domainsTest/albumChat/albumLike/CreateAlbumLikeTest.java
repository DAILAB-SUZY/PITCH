package org.cosmic.backend.domainsTest.albumChat.albumLike;

import lombok.extern.log4j.Log4j2;
import org.cosmic.backend.domain.albumChat.dtos.albumChat.AlbumChatResponse;
import org.cosmic.backend.domain.albumChat.dtos.albumlike.AlbumChatAlbumLikeDto;
import org.cosmic.backend.domain.albumChat.repositorys.AlbumChatRepository;
import org.cosmic.backend.domain.auth.dtos.UserLogin;
import org.cosmic.backend.domain.playList.domains.Album;
import org.cosmic.backend.domain.playList.repositorys.AlbumRepository;
import org.cosmic.backend.domain.playList.repositorys.ArtistRepository;
import org.cosmic.backend.domain.playList.repositorys.TrackRepository;
import org.cosmic.backend.domain.post.dtos.Post.AlbumDto;
import org.cosmic.backend.domain.user.domains.User;
import org.cosmic.backend.domain.user.repositorys.EmailRepository;
import org.cosmic.backend.domain.user.repositorys.UsersRepository;
import org.cosmic.backend.domainsTest.BaseSetting;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Log4j2
public class CreateAlbumLikeTest extends BaseSetting {

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
    @Autowired
    AlbumChatRepository albumChatRepository;

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Test
    @Transactional
    @Sql("/data/albumChat.sql")
    public void albumLikeCreateTest() throws Exception {
        User user=userRepository.findByEmail_Email("test1@example.com").get();
        user.setPassword(encoder.encode(user.getPassword()));
        UserLogin userLogin = loginUser("test1@example.com");
        Album album=albumRepository.findByTitleAndArtist_ArtistName("bam","bibi").get();

        AlbumDto albumDto = AlbumDto.createAlbumDto(album.getAlbumId());
        ResultActions resultActions =mockMvcHelper("/api/albumchat/open",albumDto);
        MvcResult result = resultActions.andReturn();
        String content = result.getResponse().getContentAsString();
        AlbumChatResponse albumChatResponse = mapper.readValue(content, AlbumChatResponse.class);
        Long albumChatId = albumChatResponse.getAlbumChatId();

        AlbumChatAlbumLikeDto albumChatAlbumLikeDto=AlbumChatAlbumLikeDto.createAlbumChatAlbumLikeDto
            (user.getUserId(),albumChatId);
        mockMvcHelper("/api/albumchat/albumlike/create",albumChatAlbumLikeDto)
            .andExpect(status().isOk());
    }

    @Test
    @Transactional
    @Sql("/data/albumChat.sql")
    public void notMatchAlbumLikeCreateTest() throws Exception {
        User user=userRepository.findByEmail_Email("test1@example.com").get();
        user.setPassword(encoder.encode(user.getPassword()));
        UserLogin userLogin = loginUser("test1@example.com");
        Album album=albumRepository.findByTitleAndArtist_ArtistName("bam","bibi").get();
        AlbumDto albumDto = AlbumDto.createAlbumDto(album.getAlbumId());
        mockMvcHelper("/api/albumchat/open",albumDto);

        AlbumChatAlbumLikeDto albumChatAlbumLikeDto=AlbumChatAlbumLikeDto.createAlbumChatAlbumLikeDto
            (user.getUserId(),100L);
        mockMvcHelper("/api/albumchat/albumlike/create",albumChatAlbumLikeDto)
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    @Sql("/data/albumChat.sql")
    public void albumlikeExistTest() throws Exception {
        User user=userRepository.findByEmail_Email("test1@example.com").get();
        user.setPassword(encoder.encode(user.getPassword()));
        UserLogin userLogin = loginUser("test1@example.com");
        String validToken=userLogin.getToken();
        Album album=albumRepository.findByTitleAndArtist_ArtistName("bam","bibi").get();

        AlbumDto albumDto = AlbumDto.createAlbumDto(album.getAlbumId());
        ResultActions resultActions =mockMvcHelper("/api/albumchat/open",albumDto);
        MvcResult result = resultActions.andReturn();
        String content = result.getResponse().getContentAsString();
        AlbumChatResponse albumChatResponse = mapper.readValue(content, AlbumChatResponse.class);
        Long albumChatId = albumChatResponse.getAlbumChatId();

        AlbumChatAlbumLikeDto albumChatAlbumLikeDto=AlbumChatAlbumLikeDto.createAlbumChatAlbumLikeDto
            (user.getUserId(),albumChatId);
        mockMvcHelper("/api/albumchat/albumlike/create",albumChatAlbumLikeDto);

        mockMvc.perform(post("/api/albumchat/albumlike/create")
                .header("Authorization", "Bearer " + validToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(albumChatAlbumLikeDto))).andDo(print())
            .andExpect(status().isConflict());
    }
}
