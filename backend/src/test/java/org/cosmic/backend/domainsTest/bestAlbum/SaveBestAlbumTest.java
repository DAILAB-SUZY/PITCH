package org.cosmic.backend.domainsTest.bestAlbum;

import lombok.extern.log4j.Log4j2;
import org.cosmic.backend.domain.auth.dtos.UserLogin;
import org.cosmic.backend.domain.bestAlbum.dtos.BestAlbumDetail;
import org.cosmic.backend.domain.bestAlbum.dtos.BestAlbumDto;
import org.cosmic.backend.domain.bestAlbum.dtos.BestAlbumListDto;
import org.cosmic.backend.domain.playList.domains.Album;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import java.util.Arrays;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Log4j2
public class SaveBestAlbumTest extends BaseSetting {
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

    @Test
    @Transactional
    @Sql("/data/bestAlbum.sql")
    public void bestAlbumSaveTest() throws Exception {
        User user=userRepository.findByEmail_Email("test1@example.com").get();
        user.setPassword(encoder.encode(user.getPassword()));
        UserLogin userLogin = loginUser("test1@example.com");
        Album album1=albumRepository.findByTitleAndArtist_ArtistName("bam","bibi").get();
        Album album2=albumRepository.findByTitleAndArtist_ArtistName("lilac","IU").get();

        BestAlbumListDto bestalbumListDTO=new BestAlbumListDto(user.getUserId());
        bestalbumListDTO.setBestalbum(Arrays.asList(new BestAlbumDetail(album1.getAlbumId()),
            new BestAlbumDetail(album2.getAlbumId())));

        BestAlbumDto bestAlbumDto=BestAlbumDto.createBestAlbumDto(user.getUserId(),album1.getAlbumId());
        mockMvcHelper("/api/bestAlbum/add/{albumId}",album1.getAlbumId(),bestAlbumDto,userLogin.getToken())
                .andExpect(status().isOk());
        bestAlbumDto=BestAlbumDto.createBestAlbumDto(user.getUserId(),album2.getAlbumId());
        mockMvcHelper("/api/bestAlbum/add/{albumId}",album2.getAlbumId(),bestAlbumDto,userLogin.getToken())
                .andExpect(status().isOk());

        BestAlbumListDto bestAlbumListDto=BestAlbumListDto.createBestAlbumListDto
            (bestalbumListDTO.getUserId(),bestalbumListDTO.getBestalbum());
        mockMvcHelper("/api/bestAlbum/save",bestAlbumListDto,userLogin.getToken());

        mockMvcGetHelper("/api/bestAlbum/give/{userId}",user.getUserId(),userLogin.getToken()).andExpect(status().isOk());
}

    @Test
    @Transactional
    @Sql("/data/bestAlbum.sql")
    public void notMatchBestAlbumSaveTest() throws Exception {

        User user=userRepository.findByEmail_Email("test1@example.com").get();
        user.setPassword(encoder.encode(user.getPassword()));
        UserLogin userLogin = loginUser("test1@example.com");
        Album album1=albumRepository.findByTitleAndArtist_ArtistName("bam","bibi").get();
        Album album2=albumRepository.findByTitleAndArtist_ArtistName("lilac","IU").get();

        BestAlbumListDto bestalbumListDTO=new BestAlbumListDto(user.getUserId());
        bestalbumListDTO.setBestalbum(Arrays.asList(new BestAlbumDetail(album1.getAlbumId()),
                new BestAlbumDetail(album2.getAlbumId())));

        BestAlbumDto bestAlbumDto=BestAlbumDto.createBestAlbumDto(user.getUserId(),album1.getAlbumId());
        mockMvcHelper("/api/bestAlbum/add/{albumId}",album1.getAlbumId(),bestAlbumDto,userLogin.getToken())
                .andExpect(status().isOk());

        BestAlbumListDto bestAlbumListDto=BestAlbumListDto.createBestAlbumListDto
                (bestalbumListDTO.getUserId(),bestalbumListDTO.getBestalbum());
        mockMvcHelper("/api/bestAlbum/save",bestAlbumListDto,userLogin.getToken()).andExpect(status().isBadRequest());
    }
}