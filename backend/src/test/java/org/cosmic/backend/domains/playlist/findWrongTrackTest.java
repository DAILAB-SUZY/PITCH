/*package org.cosmic.backend.domains.playlist;

import lombok.extern.log4j.Log4j2;
import org.cosmic.backend.domain.auth.dto.UserLogin;
import org.cosmic.backend.domain.musicDNA.domain.MusicDna;
import org.cosmic.backend.domain.musicDNA.dto.DNADetail;
import org.cosmic.backend.domain.musicDNA.dto.DnaDTO;
import org.cosmic.backend.domain.musicDNA.repository.EmotionRepository;
import org.cosmic.backend.domain.playList.domain.Album;
import org.cosmic.backend.domain.playList.domain.Artist;
import org.cosmic.backend.domain.playList.domain.Playlist;
import org.cosmic.backend.domain.playList.domain.Track;
import org.cosmic.backend.domain.playList.dto.ArtistDTO;
import org.cosmic.backend.domain.playList.dto.trackDTO;
import org.cosmic.backend.domain.playList.repository.AlbumRepository;
import org.cosmic.backend.domain.playList.repository.ArtistRepository;
import org.cosmic.backend.domain.playList.repository.PlaylistRepository;
import org.cosmic.backend.domain.playList.repository.TrackRepository;
import org.cosmic.backend.domain.user.domain.Email;
import org.cosmic.backend.domain.user.domain.User;
import org.cosmic.backend.domain.user.repository.EmailRepository;
import org.cosmic.backend.domain.user.repository.UsersRepository;
import org.cosmic.backend.domains.BaseSetting;
import org.junit.jupiter.api.Test;
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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Log4j2
public class findWrongTrackTest extends BaseSetting {
    @Autowired
    EmailRepository emailRepository;
    @Autowired
    UsersRepository userRepository;
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
    private MockMvc mockMvc;
    ObjectMapper mapper = new ObjectMapper();

   @Test
    public void findWrongInfoTest() throws Exception {
        List<MusicDna> DNA= Arrays.asList(new MusicDna("느긋한"),new MusicDna("신나는"),new MusicDna("조용한"),new MusicDna("청순한"));
        for(int i=0;i<4;i++)
        {
            emotionRepository.save(DNA.get(i));
        }

        DnaDTO dnaDTO=new DnaDTO();
        dnaDTO.setKey(1L);
        dnaDTO.setDna(Arrays.asList(new DNADetail(1L),new DNADetail(2L),new DNADetail(3L),new DNADetail(4L)));

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        Email email = emailRepository.save(Email.builder()
                .email("3@example.com")
                .verificationCode("12345678")
                .verified(true)
                .build());

       User user=userRepository.save(User.builder()
               .email(email)
               .username("goodwill")
               .password(encoder.encode("12345678"))
               .build());

        mockMvc.perform(post("/api/dna/save")
                .contentType("application/json")
                .content(mapper.writeValueAsString(DnaDTO.builder()
                        .key(dnaDTO.getKey())
                        .dna(dnaDTO.getDna())
                        .build()
                )));

        Instant now = Instant.now();
        Artist artist = new Artist("비비");
        artistRepository.save(artist);

        Album album= new Album("발라드","밤양갱","base",artist, now);
        albumRepository.save(album);

        Track track= new Track("발라드","밤양갱","base",artist, now,album);
        trackRepository.save(track);

        Playlist playlist =new Playlist(now,now,user);
        playlistRepository.save(playlist);
        UserLogin userLogin = UserLogin.builder()
                .email("3@example.com")
                .password("12345678")
                .build();

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(userLogin)));

        MvcResult result = resultActions.andReturn();
        String validToken = mapper.readValue(result.getResponse().getContentAsString(), UserLogin.class).getToken();

        mockMvc.perform(post("/api/playlist/Tracksearch")
                        .header("Authorization", "Bearer " + validToken)
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(trackDTO.builder()
                                .trackName("hi")
                                .build()
                        )))
                .andDo(print())
                .andExpect(status().isUnauthorized());

        mockMvc.perform(post("/api/playlist/Artistsearch")
                        .header("Authorization", "Bearer " + validToken)
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(ArtistDTO.builder()
                                .artistName("아이")
                                .build()
                        )))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

}*/
