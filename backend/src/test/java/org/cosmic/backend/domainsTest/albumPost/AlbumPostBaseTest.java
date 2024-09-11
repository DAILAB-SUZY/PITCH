package org.cosmic.backend.domainsTest.albumPost;

import lombok.extern.log4j.Log4j2;
import org.cosmic.backend.domain.auth.applications.TokenProvider;
import org.cosmic.backend.domain.post.entities.Post;
import org.cosmic.backend.domain.post.repositories.PostRepository;
import org.cosmic.backend.domain.user.domains.User;
import org.cosmic.backend.domain.user.repositorys.UsersRepository;
import org.cosmic.backend.domainsTest.BaseSetting;
import org.cosmic.backend.domainsTest.UrlGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.HashMap;
import java.util.Map;

@Log4j2
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Sql(scripts = "/data/albumPost.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
public class AlbumPostBaseTest extends BaseSetting {
    @Autowired
    private UsersRepository userRepository;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private PostRepository postRepository;

    protected UrlGenerator urlGenerator=new UrlGenerator();
    protected Map<String,Object> params= new HashMap<>();

    protected User user;
    protected Post post;
    protected String validToken;

    protected final String baseUrl = "/api/album/post/{postId}";

    @BeforeEach
    public void setUp() {
        if(user == null){
            user = userRepository.findByEmail_Email("test1@example.com").orElseThrow();
        }
        if(post == null){
            post = postRepository.findByContent("밤양갱 노래좋다").orElseThrow();
        }
        validToken = tokenProvider.create(user);
        params.clear();
        params.put("postId", post.getPostId());
    }
}
