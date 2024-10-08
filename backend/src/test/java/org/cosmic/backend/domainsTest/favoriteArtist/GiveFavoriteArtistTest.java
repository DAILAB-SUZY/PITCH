package org.cosmic.backend.domainsTest.favoriteArtist;

import lombok.extern.log4j.Log4j2;
import org.cosmic.backend.domain.auth.dtos.UserLoginDetail;
import org.cosmic.backend.domain.favoriteArtist.dtos.FavoriteRequest;
import org.cosmic.backend.domain.playList.domains.Album;
import org.cosmic.backend.domain.playList.domains.Artist;
import org.cosmic.backend.domain.playList.domains.Track;
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
public class GiveFavoriteArtistTest extends BaseSetting {
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
    UrlGenerator urlGenerator=new UrlGenerator();
    Map<String,Object> params= new HashMap<>();

    @Test
    @Transactional
    @Sql("/data/favoriteArtist.sql")
    public void favoriteArtistGiveTest() throws Exception {
        User user=userRepository.findByEmail_Email("test1@example.com").get();
        user.setPassword(encoder.encode(user.getPassword()));
        UserLoginDetail userLogin = loginUser("test1@example.com");
        Artist artist=artistRepository.findByArtistName("bibi").get();
        Album album=albumRepository.findByTitleAndArtist_ArtistName("bam","bibi").get();
        Track track=trackRepository.findByTitle("bam").get();

        FavoriteRequest favoriteRequest = FavoriteRequest.createFavoriteReq
            (artist.getArtistId(),album.getAlbumId(),track.getTrackId(),album.getAlbumCover());
        mockMvcHelper(HttpMethod.POST,"/api/favoriteArtist", favoriteRequest,userLogin.getToken());

        params.put("userId",user.getUserId());
        String url=urlGenerator.buildUrl("/api/user/{userId}/favoriteArtist",params);
        mockMvcHelper(HttpMethod.GET,url,null,userLogin.getToken())
            .andExpect(status().isOk());
    }
}
