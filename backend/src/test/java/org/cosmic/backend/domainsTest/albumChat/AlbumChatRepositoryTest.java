package org.cosmic.backend.domainsTest.albumChat;

import java.util.List;
import java.util.stream.IntStream;
import lombok.extern.log4j.Log4j2;
import org.cosmic.backend.domain.albumChat.domains.AlbumChatComment;
import org.cosmic.backend.domain.albumChat.dtos.comment.AlbumChatCommentDetail;
import org.cosmic.backend.domain.albumChat.exceptions.NotFoundAlbumChatCommentException;
import org.cosmic.backend.domain.albumChat.repositorys.AlbumChatCommentRepository;
import org.cosmic.backend.domain.playList.domains.Album;
import org.cosmic.backend.domain.playList.repositorys.AlbumRepository;
import org.cosmic.backend.domain.user.domains.User;
import org.cosmic.backend.domainsTest.Creator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@Log4j2
@Import(Creator.class)
public class AlbumChatRepositoryTest {

  @Autowired
  private AlbumChatCommentRepository albumChatCommentRepository;

  @Autowired
  private AlbumRepository albumRepository;

  @Autowired
  private Creator creator;

  @Test
  @DisplayName("앨범챗 생성 테스트")
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

    AlbumChatComment albumChat = albumChatCommentRepository.save(AlbumChatComment.from(
        album,
        user,
        "goods"
    ));

    AlbumChatComment albumChatComment = AlbumChatComment.from(
        album,
        user,
        "bad"
    );
    albumChatComment.setParentAlbumChatComment(albumChat);

    albumChat.getChildComments().add(albumChatComment);

    Assertions.assertEquals("bad", albumChat.getChildComments().get(0).getContent());
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
    chat.getChildComments().addAll(
        creator.createAndSaveAlbumChatComments(chat, List.of(user, user, user, user, user), "dd"));

    List<AlbumChatComment> comments = albumChatCommentRepository.findAllByAlbum_AlbumIdAndParentAlbumChatCommentIsNull(
        album.getAlbumId());

    Assertions.assertEquals(1, comments.size());
    Assertions.assertEquals(5, comments.get(0).getChildComments().size());
    Assertions.assertEquals(5, AlbumChatCommentDetail.from(comments.get(0)).getComments().size());
  }

  @Test
  @DisplayName("앨범챗 댓글 많은 순으로 조회")
  @Transactional
  public void getAlbumChatOrderComments() {
    List<Album> albums = creator.createAndSaveAlbums(0, 5);
    User user = creator.createAndSaveUser("testman");
    albums.forEach(album -> {
      int commentCount = (int) (Math.random() * 100) % 100;
      album.getAlbumChatComments()
          .addAll(creator.createAndSaveAlbumChats(album, user, "test", 0, commentCount));
    });

    List<Album> commentOrderedAlbums = albumRepository.findAlbumsOrderByCommentCount(
        PageRequest.of(0, 5)).getContent();

    IntStream.range(1, commentOrderedAlbums.size()).forEach(i -> {
      log.info(commentOrderedAlbums.get(i).getAlbumChatComments().size());
      Assertions.assertTrue(
          commentOrderedAlbums.get(i - 1).getAlbumChatComments().size() >= commentOrderedAlbums.get(
              i).getAlbumChatComments().size());
    });
  }

  @Test
  @DisplayName("좋아요 많은 순으로 조회")
  @Transactional
  public void getAlbumChatOrderLikes() {
    List<Album> albums = creator.createAndSaveAlbums(0, 5);
    albums.forEach(album -> {
      int commentCount = (int) (Math.random() * 100) % 100;
      album.getAlbumLike()
          .addAll(creator.createAndSaveAlbumLikes(album,
              creator.createAndSaveUsers(commentCount, album.getTitle())));
    });

    List<Album> likeOrderedAlbums = albumRepository.findAlbumsOrderByAlbumLikeCount(
        PageRequest.of(0, 5)).getContent();

    IntStream.range(1, likeOrderedAlbums.size()).forEach(i -> {
      log.info(likeOrderedAlbums.get(i).getAlbumLike().size());
      Assertions.assertTrue(
          likeOrderedAlbums.get(i - 1).getAlbumLike().size() >= likeOrderedAlbums.get(i)
              .getAlbumLike().size());
    });
  }
}
