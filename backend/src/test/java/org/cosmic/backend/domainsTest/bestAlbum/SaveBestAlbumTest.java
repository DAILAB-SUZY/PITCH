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
import org.cosmic.backend.domainsTest.UrlGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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
    UrlGenerator urlGenerator=new UrlGenerator();
    Map<String,Object> params= new HashMap<>();
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

        BestAlbumListDto bestalbumListDTO=new BestAlbumListDto();
        bestalbumListDTO.setBestalbum(Arrays.asList(new BestAlbumDetail(album1.getAlbumId()),
            new BestAlbumDetail(album2.getAlbumId())));

        params.clear();
        params.put("albumId",album1.getAlbumId());
        String url=urlGenerator.buildUrl("/api/bestAlbum/{albumId}",params);
        mockMvcHelper(HttpMethod.POST,url,null,userLogin.getToken()).andExpect(status().isOk());

        params.clear();
        params.put("albumId",album2.getAlbumId());
        url=urlGenerator.buildUrl("/api/bestAlbum/{albumId}",params);
        mockMvcHelper(HttpMethod.POST,url,null,userLogin.getToken()).andExpect(status().isOk());

        BestAlbumListDto bestAlbumListDto=BestAlbumListDto.createBestAlbumListDto
            (bestalbumListDTO.getBestalbum());
        mockMvcHelper(HttpMethod.POST,"/api/bestAlbum",bestAlbumListDto,userLogin.getToken());

        mockMvcHelper(HttpMethod.GET,"/api/bestAlbum",null,userLogin.getToken()).andExpect(status().isOk());
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

        BestAlbumListDto bestalbumListDTO=new BestAlbumListDto();
        bestalbumListDTO.setBestalbum(Arrays.asList(new BestAlbumDetail(album1.getAlbumId()),
                new BestAlbumDetail(album2.getAlbumId())));

        params.clear();
        params.put("albumId",album1.getAlbumId());
        String url=urlGenerator.buildUrl("/api/bestAlbum/{albumId}",params);
        mockMvcHelper(HttpMethod.POST,url,null,userLogin.getToken()).andExpect(status().isOk());

        BestAlbumListDto bestAlbumListDto=BestAlbumListDto.createBestAlbumListDto
                (bestalbumListDTO.getBestalbum());
        mockMvcHelper(HttpMethod.POST,"/api/bestAlbum/save",bestAlbumListDto,userLogin.getToken()).andExpect(status().isBadRequest());
    }
}