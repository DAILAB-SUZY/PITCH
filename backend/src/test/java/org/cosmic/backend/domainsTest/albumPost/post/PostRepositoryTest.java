package org.cosmic.backend.domainsTest.albumPost.post;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.LongStream;
import lombok.extern.log4j.Log4j2;
import org.cosmic.backend.domain.playList.domains.Album;
import org.cosmic.backend.domain.playList.domains.Artist;
import org.cosmic.backend.domain.post.entities.Post;
import org.cosmic.backend.domain.post.repositories.PostRepository;
import org.cosmic.backend.domain.user.domains.Email;
import org.cosmic.backend.domain.user.domains.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@DataJpaTest
@Log4j2
public class PostRepositoryTest {

  @Autowired
  private PostRepository postRepository;

  @Autowired
  private TestEntityManager entityManager;

  private User makeUser(String name) {
    return entityManager.persist(User.builder().email(entityManager.persist(
            Email.builder().email("%s@email.com".formatted(name)).verificationCode("1234").build()))
        .username(name)
        .password("1234").build());
  }

  private Album makeAlbum(String albumName, String artistName) {
    return entityManager.persist(
        Album.builder().title(albumName).spotifyAlbumId("1234").albumCover("base")
            .artist(entityManager.persist(
                Artist.builder().artistName(artistName).spotifyArtistId("1234")
                    .artistCover("base").build())).build());
  }

  private Post makePost(User user, Album album, String content, Instant created, Instant updated) {
    return entityManager.persist(
        Post.builder().album(album).user(user).content(content).createTime(created)
            .updateTime(updated).build());
  }

  @Test
  @DisplayName("업데이트 및 생성 시간 기준으로 받아지는지 테스트")
  public void getPostDescTest() {
    User user = makeUser("testman");

    Album album = makeAlbum("testAlbum", "testArtist");

    LongStream.range(0, 99).forEach(
        time -> makePost(user, album, "" + time, Instant.now().minus(time, ChronoUnit.DAYS),
            time % 5 == 0 ? null : Instant.now().minus(time, ChronoUnit.HOURS)));

    Pageable pageable = PageRequest.of(0, 5);
    List<Instant> a = postRepository.findAllWithCustomSorting(pageable).getContent().stream()
        .map(post -> {
          //log.info("createTime: " + post.getCreateTime() + "\tupdateTime: " + post.getUpdateTime());
          return post.getUpdateTime() == null ? post.getCreateTime() : post.getUpdateTime();
        })
        .toList();

    for (int i = 1; i < a.size(); i++) {
      //log.info(a.get(i).toString());
      Assertions.assertTrue(a.get(i - 1).isAfter(a.get(i)));
    }
  }
}
