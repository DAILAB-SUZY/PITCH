package org.cosmic.backend.domains;

import lombok.extern.log4j.Log4j2;
import org.cosmic.backend.domain.auth.dto.UserLogin;
import org.cosmic.backend.domain.musicDNA.domain.MusicDna;
import org.cosmic.backend.domain.musicDNA.dto.DNADetail;
import org.cosmic.backend.domain.musicDNA.dto.DnaDTO;
import org.cosmic.backend.domain.musicDNA.repository.EmotionRepository;
import org.cosmic.backend.domain.playList.domain.*;
import org.cosmic.backend.domain.playList.repository.*;
import org.cosmic.backend.domain.user.domain.Email;
import org.cosmic.backend.domain.user.domain.User;
import org.cosmic.backend.domain.user.repository.EmailRepository;
import org.cosmic.backend.domain.user.repository.UsersRepository;
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
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Log4j2
public class BaseSetting {
    @Autowired
    EmailRepository emailRepository;
    @Autowired
    UsersRepository usersRepository;

    @Autowired
    ArtistRepository artistRepository;
    @Autowired
    AlbumRepository albumRepository;
    @Autowired
    TrackRepository trackRepository;
    @Autowired
    EmotionRepository emotionRepository;

    @Autowired
    PlaylistRepository playlistRepository;
    @Autowired
    AlbumTrackRepository albumTrackRepository;
    @Autowired
    playlistTrackRepository playlisttrackRepository;

    @Autowired
    TrackArtistRepository trackArtistRepository;
    @Autowired
    AlbumArtistRepository albumArtistRepository;

    @Autowired
    private MockMvc mockMvc;

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    ObjectMapper mapper = new ObjectMapper();

    private User user;
    private Email email;
    private String emailTest;
    public User RegisterUser(String emailtest){
        emailTest = emailtest;
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        email = emailRepository.save(Email.builder()
                .email(emailTest)
                .verificationCode("123456")
                .verified(true)
                .build());

        user=usersRepository.save(User.builder()
                .email(email)
                .username("goodwill")
                .password(encoder.encode("123456"))
            .build());
        return user;
    }

    public String GiveToken() throws Exception {
        UserLogin userLogin = UserLogin.builder()
                .email(emailTest)
                .password("123456")
                .build();

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userLogin)))
                .andExpect(status().isOk());

        MvcResult result = resultActions.andReturn();
        String validToken = mapper.readValue(result.getResponse().getContentAsString(), UserLogin.class).getToken();
        return validToken;
    }

    public User DNASetting(String emailtest) throws Exception {
        //userdna세팅
        emailTest=emailtest;
        List<MusicDna> DNA= Arrays.asList(new MusicDna("느긋한"),new MusicDna("신나는"),new MusicDna("조용한"),new MusicDna("청순한"));
        for(int i=0;i<4;i++)
        {
            emotionRepository.save(DNA.get(i));
        }

        DnaDTO dnaDTO=new DnaDTO();
        dnaDTO.setKey(1L);
        dnaDTO.setDna(Arrays.asList(new DNADetail(1L),new DNADetail(2L),new DNADetail(3L),new DNADetail(4L)));

        user=RegisterUser(emailTest);

        mockMvc.perform(post("/api/dna/save")
                .contentType("application/json")
                .content(mapper.writeValueAsString(DnaDTO.builder()
                        .key(dnaDTO.getKey())
                        .dna(dnaDTO.getDna())
                        .build()
                )));
        return user;
    }

    public void PlaylistSetting(){
        //playlist세팅 함수
        Instant now = Instant.now();
        Artist artist = new Artist("비비");
        artistRepository.save(artist);

        Track track= new Track("발라드","밤양갱","base",artist, now);
        trackRepository.save(track);

        Album album= new Album("발라드","밤양갱","base",artist, now);
        albumRepository.save(album);

        Playlist playlist = new Playlist(now,now,user);
        playlistRepository.save(playlist);

        Track_Artist track_artist=new Track_Artist(track,artist);
        trackArtistRepository.save(track_artist);

        Album_Track album_track=new Album_Track(track,album);
        albumTrackRepository.save(album_track);

        Album_Artist album_artist=new Album_Artist(artist,album);
        albumArtistRepository.save(album_artist);
    }
}
