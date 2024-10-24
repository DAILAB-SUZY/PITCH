package org.cosmic.backend.domainsTest.albumChat;

import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.cosmic.backend.domain.albumChat.domains.AlbumChatComment;
import org.cosmic.backend.domain.albumChat.exceptions.NotFoundAlbumChatCommentException;
import org.cosmic.backend.domain.albumChat.repositorys.AlbumChatCommentRepository;
import org.cosmic.backend.domain.playList.domains.Album;
import org.cosmic.backend.domain.user.domains.User;
import org.cosmic.backend.domainsTest.Creator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@Log4j2
@Import(Creator.class)
public class AlbumChatRepositoryTest {

  @Autowired
  private AlbumChatCommentRepository albumChatCommentRepository;

  @Autowired
  private Creator creator;

  @Test
  @DisplayName("앨범챗 생성 테스트")
  @Transactional
  public void createAlbumChatTest() {
    Album album = creator.createAndSaveAlbums("testAlbum");
    User user = creator.createAndSaveUser("testman");
    AlbumChatComment albumChatComment = albumChatCommentRepository.save(AlbumChatComment.from(
        album,
        user,
        "goods"
    ));

    Assertions.assertTrue(
        albumChatCommentRepository.findById(albumChatComment.getAlbumChatCommentId()).isPresent());
  }

  @Test
  @DisplayName("앨범챗 댓글 생성 테스트")
  @Transactional
  public void createAlbumChatCommentTest() {
    Album album = creator.createAndSaveAlbums("testAlbum");
    User user = creator.createAndSaveUser("testman");

    AlbumChatComment albumChatComment = AlbumChatComment.from(
        album,
        user,
        "bad"
    );
    albumChatComment.setParentAlbumChatComment(
        albumChatCommentRepository.save(AlbumChatComment.from(
            album,
            user,
            "goods"
        ))
    );
    AlbumChatComment created = albumChatCommentRepository.save(albumChatComment);
    Assertions.assertEquals("goods", created.getParentAlbumChatComment().getContent());
  }

  private AlbumChatComment createAndSaveAlbumChat(Album album, User user, String content) {
    return albumChatCommentRepository.save(AlbumChatComment.from(album, user, content));
  }

  @Test
  @DisplayName("앨범챗 수정 테스트")
  @Transactional
  public void updateAlbumChatTest() {
    Album album = creator.createAndSaveAlbums("testAlbum");
    User user = creator.createAndSaveUser("testman");
    AlbumChatComment albumChatComment = creator.createAndSaveAlbumChat(album, user, "good");

    albumChatComment.setContent("bad");

    AlbumChatComment updated = albumChatCommentRepository.findById(
        albumChatComment.getAlbumChatCommentId()).orElseThrow(
        NotFoundAlbumChatCommentException::new);

    Assertions.assertEquals("bad", updated.getContent());
  }

  @Test
  @DisplayName("앨범챗 조회")
  @Transactional
  public void getAlbumChatsTest() {
    Album album = creator.createAndSaveAlbums("testAlbum");
    User user = creator.createAndSaveUser("testman");

    creator.createAndSaveAlbumChats(album, user, "goods", 0, 10);

    List<AlbumChatComment> comments = albumChatCommentRepository.findByAlbum_AlbumId(
        album.getAlbumId()).get();

    Assertions.assertEquals(10, comments.size());
  }

  @Test
  @DisplayName("앨범챗 및 댓글 조회")
  @Transactional
  public void getAlbumChatDetailTest() {
    Album album = creator.createAndSaveAlbums("testAlbum");
    User user = creator.createAndSaveUser("testman");
    AlbumChatComment chat = creator.createAndSaveAlbumChat(album, user, "goods");
    creator.createAndSaveAlbumChatComments(chat, List.of(user, user, user, user, user), "dd");

    List<AlbumChatComment> comments = albumChatCommentRepository.findByAlbum_AlbumId(
        album.getAlbumId()).get();

    Assertions.assertEquals(1, comments.size());
  }
}
