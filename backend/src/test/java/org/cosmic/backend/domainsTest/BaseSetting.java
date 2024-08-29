package org.cosmic.backend.domainsTest;

import lombok.extern.log4j.Log4j2;
import org.cosmic.backend.domain.albumChat.domains.AlbumChat;
import org.cosmic.backend.domain.albumChat.repositorys.AlbumChatRepository;
import org.cosmic.backend.domain.auth.dtos.UserLogin;
import org.cosmic.backend.domain.playList.domains.Album;
import org.cosmic.backend.domain.playList.domains.Artist;
import org.cosmic.backend.domain.playList.domains.Track;
import org.cosmic.backend.domain.playList.repositorys.AlbumRepository;
import org.cosmic.backend.domain.playList.repositorys.ArtistRepository;
import org.cosmic.backend.domain.playList.repositorys.PlaylistRepository;
import org.cosmic.backend.domain.playList.repositorys.TrackRepository;
import org.cosmic.backend.domain.post.dtos.Post.CreatePost;
import org.cosmic.backend.domain.user.domains.Email;
import org.cosmic.backend.domain.user.domains.User;
import org.cosmic.backend.domain.user.repositorys.EmailRepository;
import org.cosmic.backend.domain.user.repositorys.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.time.Instant;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Log4j2
public class BaseSetting {
    @Autowired
    private MockMvc mockMvc;
    private final ObjectMapper mapper=new ObjectMapper();
    private ResultActions resultActions;
    private String validToken;

    protected UserLogin loginUser(String email) throws Exception {
        UserLogin userLogin = UserLogin.builder()
                .email(email)
                .password("12345678")
                .build();
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(userLogin)));
        MvcResult result = resultActions.andReturn();
        validToken = mapper.readValue(result.getResponse().getContentAsString(), UserLogin.class).getToken();
        return UserLogin.builder()
                .email(email)
                .token(validToken)
                .build();
    }
    protected <T> ResultActions mockMvcHelper(String url, T requestObject) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post(url)
                .header("Authorization", "Bearer " + validToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(requestObject)));
    }
}
