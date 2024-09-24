package org.cosmic.backend.domainsTest.bestAlbum;

import lombok.extern.log4j.Log4j2;
import org.cosmic.backend.domain.auth.dtos.UserLoginDetail;
import org.cosmic.backend.domain.playList.repositorys.AlbumRepository;
import org.cosmic.backend.domain.playList.repositorys.ArtistRepository;
import org.cosmic.backend.domain.playList.repositorys.TrackRepository;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Log4j2
public class SearchAlbumTest extends BaseSetting {
    @Autowired
    EmailRepository emailRepository;
    @Autowired
    UsersRepository userRepository;
    @Autowired
    TrackRepository trackRepository;
    @Autowired
    AlbumRepository albumRepository;
    @Autowired
    ArtistRepository artistRepository;
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    UrlGenerator urlGenerator=new UrlGenerator();
    Map<String,Object> params= new HashMap<>();

    @Test
    @Transactional
    @Sql("/data/albumPost.sql")
    public void albumNameSearchTest() throws Exception {
        User user=userRepository.findByEmail_Email("test1@example.com").get();
        user.setPassword(encoder.encode(user.getPassword()));
        UserLoginDetail userLogin = loginUser("test1@example.com");
        //TODO 앨범추가
        params.clear();
        params.put("albumName","bam");
        String url=urlGenerator.buildUrl("/api/bestAlbum/album/{albumName}",params);
        mockMvcHelper(HttpMethod.GET,url,null,userLogin.getToken()).andExpect(status().isOk());
    }

    @Test
    @Transactional
    @Sql("/data/albumPost.sql")
    public void artistNameSearchTest() throws Exception {
        User user=userRepository.findByEmail_Email("test1@example.com").get();
        user.setPassword(encoder.encode(user.getPassword()));
        UserLoginDetail userLogin = loginUser("test1@example.com");

        params.clear();
        params.put("albumName","bibi");
        String url=urlGenerator.buildUrl("/api/bestAlbum/album/{albumName}",params);
        mockMvcHelper(HttpMethod.GET,url,null,userLogin.getToken()).andExpect(status().isNotFound());}
}
