package org.cosmic.backend.domainsTest.favoriteArtist;

import lombok.extern.log4j.Log4j2;
import org.cosmic.backend.domain.auth.dtos.UserLogin;
import org.cosmic.backend.domain.favoriteArtist.dtos.AlbumRequest;
import org.cosmic.backend.domain.favoriteArtist.dtos.TrackRequest;
import org.cosmic.backend.domain.playList.domains.Album;
import org.cosmic.backend.domain.playList.domains.Artist;
import org.cosmic.backend.domain.playList.domains.Track;
import org.cosmic.backend.domain.playList.dtos.ArtistDto;
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
public class SearchFavoriteArtistTest extends BaseSetting {
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
    UrlGenerator urlGenerator=new UrlGenerator();
    Map<String,Object> params= new HashMap<>();
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Test
    @Transactional
    @Sql("/data/favoriteArtist.sql")
    public void artistSearchMatchTest() throws Exception {
        User user=userRepository.findByEmail_Email("test1@example.com").get();
        user.setPassword(encoder.encode(user.getPassword()));
        UserLogin userLogin = loginUser("test1@example.com");
        Artist artist=artistRepository.findByArtistName("bibi").get();

        params.clear();
        params.put("artistName",artist.getArtistName());
        String url=urlGenerator.buildUrl("/api/favoriteArtist/artist/{artistName}",params);
        mockMvcHelper(HttpMethod.GET,url,null,userLogin.getToken()).andExpect(status().isOk());
    }

    @Test
    @Transactional
    @Sql("/data/favoriteArtist.sql")
    public void notMatchArtistSearchTest() throws Exception {
        User user=userRepository.findByEmail_Email("test1@example.com").get();
        user.setPassword(encoder.encode(user.getPassword()));
        UserLogin userLogin = loginUser("test1@example.com");

        params.clear();
        params.put("artistName","bi");
        String url=urlGenerator.buildUrl("/api/favoriteArtist/artist/{artistName}",params);
        mockMvcHelper(HttpMethod.GET,url,null,userLogin.getToken()).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    @Sql("/data/favoriteArtist.sql")
    public void albumSearchMatchTest() throws Exception {
        User user=userRepository.findByEmail_Email("test1@example.com").get();
        user.setPassword(encoder.encode(user.getPassword()));
        UserLogin userLogin = loginUser("test1@example.com");
        Artist artist=artistRepository.findByArtistName("bibi").get();

        params.clear();
        params.put("artistId",artist.getArtistId());
        params.put("albumName","bam");
        String url=urlGenerator.buildUrl("/api/favoriteArtist/artist/{artistId}/album/{albumName}",params);
        mockMvcHelper(HttpMethod.GET,url,null,userLogin.getToken()).andExpect(status().isOk());
    }

    @Test
    @Transactional
    @Sql("/data/favoriteArtist.sql")
    public void notMatchAlbumSearchTest() throws Exception {
        User user=userRepository.findByEmail_Email("test1@example.com").get();
        user.setPassword(encoder.encode(user.getPassword()));
        UserLogin userLogin = loginUser("test1@example.com");

        params.clear();
        params.put("artistId",100L);
        params.put("albumName","bam");
        String url=urlGenerator.buildUrl("/api/favoriteArtist/artist/{artistId}/album/{albumName}",params);
        mockMvcHelper(HttpMethod.GET,url,null,userLogin.getToken()).andExpect(status().isNotFound());
}

    @Test
    @Transactional
    @Sql("/data/favoriteArtist.sql")
    public void trackSearchMatchTest() throws Exception {
        User user=userRepository.findByEmail_Email("test1@example.com").get();
        user.setPassword(encoder.encode(user.getPassword()));
        UserLogin userLogin = loginUser("test1@example.com");
        Album album=albumRepository.findByTitleAndArtist_ArtistName("bam","bibi").get();
        Track track=trackRepository.findByTitle("bam").get();

        params.clear();
        params.put("albumId",album.getAlbumId());
        params.put("trackName",track.getTitle());
        String url=urlGenerator.buildUrl("/api/favoriteArtist/album/{albumId}/track/{trackName}",params);
        mockMvcHelper(HttpMethod.GET,url,null,userLogin.getToken()).andExpect(status().isOk());
    }

    @Test
    @Transactional
    @Sql("/data/favoriteArtist.sql")
    public void notMatchTrackSearchTest() throws Exception {
        User user=userRepository.findByEmail_Email("test1@example.com").get();
        user.setPassword(encoder.encode(user.getPassword()));
        UserLogin userLogin = loginUser("test1@example.com");
        Album album=albumRepository.findByTitleAndArtist_ArtistName("bam","bibi").get();

        params.clear();
        params.put("albumId",album.getAlbumId());
        params.put("trackName","bamd");
        String url=urlGenerator.buildUrl("/api/favoriteArtist/album/{albumId}/track/{trackName}",params);
        mockMvcHelper(HttpMethod.GET,url,null,userLogin.getToken()).andExpect(status().isNotFound());
    }
}
