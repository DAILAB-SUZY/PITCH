package org.cosmic.backend.domainsTest.albumChat;

import java.util.HashMap;
import java.util.Map;
import lombok.extern.log4j.Log4j2;
import org.cosmic.backend.domain.albumChat.domains.AlbumChatComment;
import org.cosmic.backend.domain.albumChat.repositorys.AlbumChatCommentRepository;
import org.cosmic.backend.domain.auth.applications.TokenProvider;
import org.cosmic.backend.domain.playList.domains.Album;
import org.cosmic.backend.domain.playList.repositorys.AlbumRepository;
import org.cosmic.backend.domain.user.domains.User;
import org.cosmic.backend.domain.user.repositorys.UsersRepository;
import org.cosmic.backend.domainsTest.BaseSetting;
import org.cosmic.backend.domainsTest.UrlGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

@Log4j2
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Sql(scripts = "/data/albumChat.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)  // 클래스가 끝나면 컨텍스트 초기화
public class AlbumChatBaseTest extends BaseSetting {

  protected final String baseUrl = "/api/album/{albumId}";
  @Autowired
  protected AlbumRepository albumRepository;
  @Autowired
  protected AlbumChatCommentRepository albumChatCommentRepository;
  protected UrlGenerator urlGenerator = new UrlGenerator();
  protected Map<String, Object> params = new HashMap<>();
  protected User user;
  protected User user2;
  protected Album album;
  protected String validToken;
  protected String validToken2;
  protected AlbumChatComment albumChatComment;
  protected AlbumChatComment albumChatComment2;
  @Autowired
  private UsersRepository userRepository;
  @Autowired
  private TokenProvider tokenProvider;

  @BeforeEach
  public void setUp() {
    if (user == null) {
      user = userRepository.findByEmail_Email("test1@example.com").orElseThrow();
      user2 = userRepository.findByEmail_Email("test2@example.com").orElseThrow();
    }
    if (album == null) {
      album = albumRepository.findAllByTitle("bam").get(0);
    }
    if (albumChatComment == null) {
      albumChatComment = albumChatCommentRepository.findByAlbum_AlbumId(album.getAlbumId()).get()
          .get(0);
      albumChatComment2 = albumChatCommentRepository.findByAlbum_AlbumId(album.getAlbumId()).get()
          .get(1);
    }
    validToken = tokenProvider.create(user);
    validToken2 = tokenProvider.create(user2);
    params.clear();
    params.put("albumId", album.getAlbumId());
  }
}