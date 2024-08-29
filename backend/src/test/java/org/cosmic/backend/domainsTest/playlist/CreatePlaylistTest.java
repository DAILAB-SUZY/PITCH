package org.cosmic.backend.domainsTest.playlist;

import lombok.extern.log4j.Log4j2;
import org.cosmic.backend.domain.auth.dtos.UserLogin;
import org.cosmic.backend.domain.musicDna.repositorys.EmotionRepository;
import org.cosmic.backend.domain.playList.domains.Track;
import org.cosmic.backend.domain.playList.dtos.PlaylistDetail;
import org.cosmic.backend.domain.playList.dtos.PlaylistDto;
import org.cosmic.backend.domain.playList.dtos.TrackDto;
import org.cosmic.backend.domain.playList.repositorys.AlbumRepository;
import org.cosmic.backend.domain.playList.repositorys.ArtistRepository;
import org.cosmic.backend.domain.playList.repositorys.PlaylistRepository;
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
import java.util.List;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Log4j2
public class CreatePlaylistTest extends BaseSetting {
    @Autowired
    EmotionRepository emotionRepository;
    @Autowired
    UsersRepository userRepository;
    @Autowired
    EmailRepository emailRepository;
    @Autowired
    ArtistRepository artistRepository;
    @Autowired
    AlbumRepository albumRepository;
    @Autowired
    PlaylistRepository playlistRepository;
    @Autowired
    TrackRepository trackRepository;
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Test
    @Transactional
    @Sql("/data/playlist.sql")
    public void playlistSaveTest() throws Exception {
        User user=userRepository.findByEmail_Email("test1@example.com").get();
        user.setPassword(encoder.encode(user.getPassword()));
        UserLogin userLogin = loginUser("test1@example.com");
        Track track=trackRepository.findByTitle("bam").get();
        PlaylistDto playlistdto=new PlaylistDto(user.getUserId(),List.of(new PlaylistDetail(track.getTrackId())));
        mockMvcHelper("/api/playlist/save",playlistdto).andExpect(status().isOk());
    }

    @Test
    @Transactional
    @Sql("/data/playlist.sql")
    public void rightInfoFindTest() throws Exception {
        User user=userRepository.findByEmail_Email("test1@example.com").get();
        user.setPassword(encoder.encode(user.getPassword()));
        UserLogin userLogin = loginUser("test1@example.com");
        Track track=trackRepository.findByTitle("bam").get();

        TrackDto trackDto=TrackDto.createTrackDto(track.getTitle());
        mockMvcHelper("/api/playlist/Tracksearch",trackDto).andExpect(status().isOk());
    }

    @Test
    @Transactional
    @Sql("/data/playlist.sql")
    public void wrongInfoFindTest() throws Exception {
        User user=userRepository.findByEmail_Email("test1@example.com").get();
        user.setPassword(encoder.encode(user.getPassword()));
        UserLogin userLogin = loginUser("test1@example.com");

        TrackDto trackDto=TrackDto.createTrackDto("id");
        mockMvcHelper("/api/playlist/Tracksearch",trackDto).andExpect(status().isNotFound());
    }
}
