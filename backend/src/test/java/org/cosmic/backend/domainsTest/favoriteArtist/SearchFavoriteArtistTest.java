package org.cosmic.backend.domainsTest.favoriteArtist;

import lombok.extern.log4j.Log4j2;
import org.cosmic.backend.domain.albumChat.repositorys.AlbumChatRepository;
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
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
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
    @Autowired
    AlbumChatRepository albumChatRepository;

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Test
    @Transactional
    @Sql("/data/favoriteArtist.sql")
    public void artistSearchMatchTest() throws Exception {
        User user=userRepository.findByEmail_Email("test1@example.com").get();
        user.setPassword(encoder.encode(user.getPassword()));
        UserLogin userLogin = loginUser("test1@example.com");
        Artist artist=artistRepository.findByArtistName("bibi").get();

        ArtistDto artistDto=ArtistDto.createArtistDto(artist.getArtistName());
        mockMvcHelper("/api/favoriteArtist/searchartist",artistDto).andExpect(status().isOk());
    }
    @Test
    @Transactional
    @Sql("/data/favoriteArtist.sql")
    public void notMatchArtistSearchTest() throws Exception {
        User user=userRepository.findByEmail_Email("test1@example.com").get();
        user.setPassword(encoder.encode(user.getPassword()));
        UserLogin userLogin = loginUser("test1@example.com");

        ArtistDto artistDto=ArtistDto.createArtistDto("bi");
        mockMvcHelper("/api/favoriteArtist/searchartist",artistDto).andExpect(status().isNotFound());
    }
    @Test
    @Transactional
    @Sql("/data/favoriteArtist.sql")
    public void albumSearchMatchTest() throws Exception {
        User user=userRepository.findByEmail_Email("test1@example.com").get();
        user.setPassword(encoder.encode(user.getPassword()));
        UserLogin userLogin = loginUser("test1@example.com");
        Artist artist=artistRepository.findByArtistName("bibi").get();
        Album album=albumRepository.findByTitleAndArtist_ArtistName("bam","bibi").get();

        AlbumRequest albumRequest=AlbumRequest.createAlbumRequest(artist.getArtistId(),album.getTitle());
        mockMvcHelper("/api/favoriteArtist/searchalbum",albumRequest).andExpect(status().isOk());
    }

    @Test
    @Transactional
    @Sql("/data/favoriteArtist.sql")
    public void notMatchAlbumSearchTest() throws Exception {
        User user=userRepository.findByEmail_Email("test1@example.com").get();
        user.setPassword(encoder.encode(user.getPassword()));
        UserLogin userLogin = loginUser("test1@example.com");
        Artist artist=artistRepository.findByArtistName("bibi").get();
        Album album=albumRepository.findByTitleAndArtist_ArtistName("bam","bibi").get();

        AlbumRequest albumRequest=AlbumRequest.createAlbumRequest(artist.getArtistId(),"ba");
        mockMvcHelper("/api/favoriteArtist/searchalbum",albumRequest).andExpect(status().isNotFound());
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

        TrackRequest trackRequest=TrackRequest.createTrackRequest(album.getAlbumId(),track.getTitle());
        mockMvcHelper("/api/favoriteArtist/searchtrack",trackRequest).andExpect(status().isOk());
    }

    @Test
    @Transactional
    @Sql("/data/favoriteArtist.sql")
    public void notMatchTrackSearchTest() throws Exception {
        User user=userRepository.findByEmail_Email("test1@example.com").get();
        user.setPassword(encoder.encode(user.getPassword()));
        UserLogin userLogin = loginUser("test1@example.com");
        Album album=albumRepository.findByTitleAndArtist_ArtistName("bam","bibi").get();
        Track track=trackRepository.findByTitle("bam").get();

        TrackRequest trackRequest=TrackRequest.createTrackRequest(album.getAlbumId(),"bamd");
        mockMvcHelper("/api/favoriteArtist/searchtrack",trackRequest).andExpect(status().isNotFound());
    }
}
