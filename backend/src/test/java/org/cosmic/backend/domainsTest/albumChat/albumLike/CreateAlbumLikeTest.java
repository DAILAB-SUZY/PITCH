package org.cosmic.backend.domainsTest.albumChat.albumLike;

import lombok.extern.log4j.Log4j2;
import org.cosmic.backend.domain.auth.dtos.UserLoginDetail;
import org.cosmic.backend.domain.playList.domains.Album;
import org.cosmic.backend.domain.playList.repositorys.AlbumRepository;
import org.cosmic.backend.domain.playList.repositorys.ArtistRepository;
import org.cosmic.backend.domain.playList.repositorys.TrackRepository;
import org.cosmic.backend.domain.user.domains.User;
import org.cosmic.backend.domain.user.repositorys.EmailRepository;
import org.cosmic.backend.domain.user.repositorys.UsersRepository;
import org.cosmic.backend.domainsTest.BaseSetting;
import org.cosmic.backend.domainsTest.UrlGenerator;
import org.cosmic.backend.domainsTest.albumChat.AlbumChatBaseTest;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Log4j2
public class CreateAlbumLikeTest extends AlbumChatBaseTest {
    private final String baseUrl = super.baseUrl;

    @Test
    @Order(1)
    public void albumLikeCreateTest() throws Exception {
        mockMvcHelper(HttpMethod.POST,urlGenerator.buildUrl(baseUrl+"/albumLike",params),null,validToken)
            .andExpect(status().isOk());
    }
    @Test
    @Order(2)
    public void albumLikesGiveTest() throws Exception {
        mockMvcHelper(HttpMethod.GET,urlGenerator.buildUrl(baseUrl+"/albumLike",params),null,validToken)
            .andExpect(status().isOk());
    }

    @Test
    @Order(3)
    public void albumlikeExistTest() throws Exception {
        mockMvcHelper(HttpMethod.POST,urlGenerator.buildUrl(baseUrl+"/albumLike",params),null,validToken)
                .andExpect(status().isConflict());
    }

    @Test
    @Transactional
    @Order(4)
    public void albumlikeDeleteTest() throws Exception {
        mockMvcHelper(HttpMethod.DELETE,urlGenerator.buildUrl(baseUrl+"/albumLike",params),null,validToken)
                .andExpect(status().isOk());
    }

}
