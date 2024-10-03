package org.cosmic.backend.domainsTest.playlist;

import lombok.extern.log4j.Log4j2;
import org.cosmic.backend.domain.auth.dtos.UserLoginDetail;
import org.cosmic.backend.domain.musicDna.repositorys.MusicDnaRepository;
import org.cosmic.backend.domain.playList.domains.Track;
import org.cosmic.backend.domain.playList.dtos.PlaylistDetail;
import org.cosmic.backend.domain.playList.dtos.PlaylistDto;
import org.cosmic.backend.domain.playList.repositorys.AlbumRepository;
import org.cosmic.backend.domain.playList.repositorys.ArtistRepository;
import org.cosmic.backend.domain.playList.repositorys.PlaylistRepository;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Log4j2
public class CreatePlaylistTest extends BaseSetting {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    MusicDnaRepository musicDnaRepository;
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

    UrlGenerator urlGenerator= new UrlGenerator();
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Test
    @Transactional
    @Sql("/data/playlist.sql")
    public void playlistSaveTest() throws Exception {
        User user=userRepository.findByEmail_Email("test1@example.com").get();
        user.setPassword(encoder.encode(user.getPassword()));
        UserLoginDetail userLogin = loginUser("test1@example.com");
        Track track=trackRepository.findByTitle("bam").get();
        PlaylistDto playlistdto=new PlaylistDto(List.of((track.getTrackId())));

        mockMvcHelper(HttpMethod.POST,"/api/playlist",playlistdto,userLogin.getToken()).andExpect(status().isOk());
    }

    @Test
    @Transactional
    @Sql("/data/playlist.sql")
    public void rightInfoFindTest() throws Exception {
        User user=userRepository.findByEmail_Email("test1@example.com").get();
        user.setPassword(encoder.encode(user.getPassword()));
        UserLoginDetail userLogin = loginUser("test1@example.com");

        Map<String,Object> params= new HashMap<>();
        params.put("trackName","bam");

        String url=urlGenerator.buildUrl("/api/playlist/track/{trackName}",params);
        mockMvcHelper(HttpMethod.GET,url,null,userLogin.getToken()).andExpect(status().isOk());
   }

    @Test
    @Transactional
    @Sql("/data/playlist.sql")
    public void wrongInfoFindTest() throws Exception {
        User user=userRepository.findByEmail_Email("test1@example.com").get();
        user.setPassword(encoder.encode(user.getPassword()));
        UserLoginDetail userLogin = loginUser("test1@example.com");

        Map<String,Object> params= new HashMap<>();
        params.put("trackName","ifsds");

        String url=urlGenerator.buildUrl("/api/playlist/track/{trackName}",params);
        mockMvcHelper(HttpMethod.GET,url,null,userLogin.getToken()).andExpect(status().isNotFound());
    }
}
