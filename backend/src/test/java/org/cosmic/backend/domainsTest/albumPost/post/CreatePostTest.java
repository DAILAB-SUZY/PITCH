package org.cosmic.backend.domainsTest.albumPost.post;

import lombok.extern.log4j.Log4j2;
import org.cosmic.backend.domain.auth.dtos.UserLogin;
import org.cosmic.backend.domain.playList.dtos.ArtistDto;
import org.cosmic.backend.domain.playList.repositorys.AlbumRepository;
import org.cosmic.backend.domain.playList.repositorys.ArtistRepository;
import org.cosmic.backend.domain.playList.repositorys.TrackRepository;
import org.cosmic.backend.domain.post.dtos.Post.AlbumDto;
import org.cosmic.backend.domain.post.dtos.Post.CreatePost;
import org.cosmic.backend.domain.post.dtos.Post.PostDto;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Log4j2
public class CreatePostTest extends BaseSetting {
    final ObjectMapper mapper = new ObjectMapper();
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
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private MvcResult result;
    UrlGenerator urlGenerator=new UrlGenerator();
    Map<String,Object> params= new HashMap<>();

    @Test
    @Transactional
    @Sql("/data/albumPost.sql")
    public void albumSearchTest() throws Exception {
        User user=userRepository.findByEmail_Email("test1@example.com").get();
        user.setPassword(encoder.encode(user.getPassword()));
        UserLogin userLogin = loginUser("test1@example.com");

        AlbumDto albumDto=AlbumDto.createAlbumDto("bam");
        String url="/api/post/searchAlbum";
        mockMvcHelper(HttpMethod.POST,url,albumDto,userLogin.getToken()).andExpect(status().isOk());

        ArtistDto artistDto=ArtistDto.createArtistDto("bibi");
        url="/api/post/searchArtist";
        mockMvcHelper(HttpMethod.POST,url,artistDto,userLogin.getToken()).andExpect(status().isOk());
    }
    //아티스트 또는 앨범으로 찾기

    @Test
    @Transactional
    @Sql("/data/albumPost.sql")
    public void createPostTest() throws Exception {
        User user=userRepository.findByEmail_Email("test1@example.com").get();
        user.setPassword(encoder.encode(user.getPassword()));
        UserLogin userLogin = loginUser("test1@example.com");
        CreatePost createPost=CreatePost.createCreatePost
            (user.getUserId(),"base","bibi","밤양갱 노래좋다","bam",null);
        String url="/api/post/create";
        mockMvcHelper(HttpMethod.POST,url,createPost,userLogin.getToken())
            .andExpect(status().isOk());
    }

    //post잘 만들어지는지
    @Test
    @Transactional
    @Sql("/data/albumPost.sql")
    public void givePostTest() throws Exception {
        User user=userRepository.findByEmail_Email("test1@example.com").get();
        user.setPassword(encoder.encode(user.getPassword()));
        UserLogin userLogin = loginUser("test1@example.com");

        CreatePost createPost=CreatePost.createCreatePost
            (user.getUserId(),"base","bibi","밤양갱 노래좋다","bam",null);
        String url="/api/post/create";
        ResultActions resultActions =mockMvcHelper(HttpMethod.POST,url,createPost,userLogin.getToken());
        result = resultActions.andReturn();
        String content = result.getResponse().getContentAsString();
        PostDto postDto = mapper.readValue(content, PostDto.class);
        Long postId = postDto.getPostId();

        UserDto userDto=UserDto.createUserDto(user.getUserId());
        url="/api/post/give";
        mockMvcHelper(HttpMethod.POST,url,userDto,userLogin.getToken()).andExpect(status().isOk());
        PostDto postDto1=PostDto.createPostDto(postId);
        url="/api/post/open";
        mockMvcHelper(HttpMethod.POST,url,postDto1,userLogin.getToken()).andExpect(status().isOk());
    }
}
