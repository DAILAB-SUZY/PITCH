package org.cosmic.backend.domainsTest.bestAlbum;

import lombok.extern.log4j.Log4j2;
import org.cosmic.backend.domain.auth.dtos.UserLogin;
import org.cosmic.backend.domain.bestAlbum.dtos.BestAlbumDetail;
import org.cosmic.backend.domain.bestAlbum.dtos.BestAlbumDto;
import org.cosmic.backend.domain.bestAlbum.dtos.BestAlbumListDto;
import org.cosmic.backend.domain.playList.domains.Album;
import org.cosmic.backend.domain.playList.domains.Artist;
import org.cosmic.backend.domain.playList.repositorys.AlbumRepository;
import org.cosmic.backend.domain.playList.repositorys.ArtistRepository;
import org.cosmic.backend.domain.playList.repositorys.TrackRepository;
import org.cosmic.backend.domain.user.domains.User;
import org.cosmic.backend.domain.user.dtos.UserDto;
import org.cosmic.backend.domain.user.repositorys.EmailRepository;
import org.cosmic.backend.domain.user.repositorys.UsersRepository;
import org.cosmic.backend.domainsTest.BaseSetting;
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
    final ObjectMapper mapper = new ObjectMapper();

    @Test
    @Transactional
    public void bestAlbumSaveTest() throws Exception {
        UserLogin userLogin = loginUser("test@example.com");
        String validToken=userLogin.getToken();
        Instant now = Instant.now();
        User user=getUser();
        Artist artist=saveArtist("비비");
        Artist artist2=saveArtist("아이유");

        Album album=saveAlbum("밤양갱", artist, now);
        Album album2=saveAlbum("celebrity", artist2, now);

        BestAlbumListDto bestalbumListDTO=new BestAlbumListDto();
        bestalbumListDTO.setUserId(user.getUserId());
        bestalbumListDTO.setBestalbum(Arrays.asList(new BestAlbumDetail(album.getAlbumId()),
            new BestAlbumDetail(album2.getAlbumId())));

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
            .content(mapper.writeValueAsString(BestAlbumListDto.builder()
                .userId(bestalbumListDTO.getUserId())
                .bestalbum(bestalbumListDTO.getBestalbum())
                .build()
            )));
        mockMvc.perform(post("/api/bestAlbum/give")
                .header("Authorization", "Bearer " + validToken)
                .contentType("application/json")
                .content(mapper.writeValueAsString(UserDto.builder()
                    .userId(user.getUserId())
                    .build()))) // build() 메서드를 호출하여 최종 객체를 생성
            .andExpect(status().isOk());
}

    @Test
    @Transactional
    public void notMatchBestAlbumSaveTest() throws Exception {
        UserLogin userLogin = loginUser("test@example.com");
        String validToken=userLogin.getToken();
        Instant now = Instant.now();
        User user=getUser();
        Artist artist=saveArtist("비비");
        Artist artist2=saveArtist("아이유");

        Album album=saveAlbum("밤양갱", artist, now);
        Album album2=saveAlbum("celebrity", artist2, now);

        BestAlbumListDto bestalbumListDTO=new BestAlbumListDto();
        bestalbumListDTO.setUserId(user.getUserId());
        bestalbumListDTO.setBestalbum(Arrays.asList(new BestAlbumDetail(album.getAlbumId()),
            new BestAlbumDetail(album2.getAlbumId())));

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
            .content(mapper.writeValueAsString(BestAlbumListDto.builder()
                .userId(bestalbumListDTO.getUserId())
                .bestalbum(bestalbumListDTO.getBestalbum())
                .build()
            ))).andDo(print()).andExpect(status().isBadRequest());
    }
}