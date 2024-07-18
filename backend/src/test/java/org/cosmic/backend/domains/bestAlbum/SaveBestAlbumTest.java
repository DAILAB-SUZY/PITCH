package org.cosmic.backend.domains.bestAlbum;

import lombok.extern.log4j.Log4j2;
import org.cosmic.backend.domain.auth.dto.UserLogin;
import org.cosmic.backend.domain.bestAlbum.dto.BestAlbumDto;
import org.cosmic.backend.domain.bestAlbum.dto.BestAlbumListDTO;
import org.cosmic.backend.domain.bestAlbum.dto.bestAlbumDetail;
import org.cosmic.backend.domain.playList.domain.Album;
import org.cosmic.backend.domain.playList.domain.Artist;
import org.cosmic.backend.domain.playList.repository.AlbumRepository;
import org.cosmic.backend.domain.playList.repository.ArtistRepository;
import org.cosmic.backend.domain.playList.repository.TrackRepository;
import org.cosmic.backend.domain.user.domain.User;
import org.cosmic.backend.domain.user.dto.userDto;
import org.cosmic.backend.domain.user.repository.EmailRepository;
import org.cosmic.backend.domain.user.repository.UsersRepository;
import org.cosmic.backend.domains.BaseSetting;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Log4j2
public class SaveBestAlbumTest extends BaseSetting {
    @Autowired
    private MockMvc mockMvc;
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
    ObjectMapper mapper = new ObjectMapper();

    @Test
    @Transactional
    public void saveBestAlbumTest() throws Exception {
        UserLogin userLogin = loginUser("test@example.com","12345678");
        String validToken=userLogin.getToken();
        Instant now = Instant.now();
        User user=getUser();
        Artist artist=saveArtist("비비");
        Artist artist2=saveArtist("아이유");

        Album album=saveAlbum("밤양갱", artist, now, "발라드");
        Album album2=saveAlbum("celebrity", artist2, now, "발라드");

        BestAlbumListDTO bestalbumListDTO=new BestAlbumListDTO();
        bestalbumListDTO.setUserId(user.getUserId());
        bestalbumListDTO.setBestalbum(Arrays.asList(new bestAlbumDetail(album.getAlbumId()),new bestAlbumDetail(album2.getAlbumId())));

        mockMvc.perform(post("/api/bestAlbum/add")
                        .header("Authorization", "Bearer " + validToken)
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(BestAlbumDto.builder()
                                .userId(user.getUserId())
                                .albumId(album.getAlbumId())
                                .build()
                        )));
        mockMvc.perform(post("/api/bestAlbum/add")
                .header("Authorization", "Bearer " + validToken)
                .contentType("application/json")
                .content(mapper.writeValueAsString(BestAlbumDto.builder()
                        .userId(user.getUserId())
                        .albumId(album2.getAlbumId())
                        .build()
                )));

        mockMvc.perform(post("/api/bestAlbum/save")
                        .header("Authorization", "Bearer " + validToken)
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(BestAlbumListDTO.builder()
                                .userId(bestalbumListDTO.getUserId())
                                .bestalbum(bestalbumListDTO.getBestalbum())
                                .build()
                        )));

        //저장 후 열람
        mockMvc.perform(post("/api/bestAlbum/give")
                        .header("Authorization", "Bearer " + validToken)
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(userDto.builder()
                                .userid(user.getUserId())
                                .build()))) // build() 메서드를 호출하여 최종 객체를 생성
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void saveNotMatchBestAlbumTest() throws Exception {
        UserLogin userLogin = loginUser("test@example.com","12345678");
        String validToken=userLogin.getToken();
        Instant now = Instant.now();
        User user=getUser();
        Artist artist=saveArtist("비비");
        Artist artist2=saveArtist("아이유");

        Album album=saveAlbum("밤양갱", artist, now, "발라드");
        Album album2=saveAlbum("celebrity", artist2, now, "발라드");

        BestAlbumListDTO bestalbumListDTO=new BestAlbumListDTO();
        bestalbumListDTO.setUserId(user.getUserId());
        bestalbumListDTO.setBestalbum(Arrays.asList(new bestAlbumDetail(album.getAlbumId()),new bestAlbumDetail(album2.getAlbumId())));

        mockMvc.perform(post("/api/bestAlbum/add")
                .header("Authorization", "Bearer " + validToken)
                .contentType("application/json")
                .content(mapper.writeValueAsString(BestAlbumDto.builder()
                        .userId(user.getUserId())
                        .albumId(album.getAlbumId())
                        .build()
                )));

        mockMvc.perform(post("/api/bestAlbum/save")
                .header("Authorization", "Bearer " + validToken)
                .contentType("application/json")
                .content(mapper.writeValueAsString(BestAlbumListDTO.builder()
                        .userId(bestalbumListDTO.getUserId())
                        .bestalbum(bestalbumListDTO.getBestalbum())
                        .build()
                ))).andDo(print()).andExpect(status().isBadRequest());
    }
}