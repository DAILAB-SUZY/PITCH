package org.cosmic.backend.domainsTest;

import java.util.List;
import java.util.stream.IntStream;
import org.cosmic.backend.domain.albumChat.domains.AlbumChatComment;
import org.cosmic.backend.domain.albumChat.domains.AlbumChatCommentLike;
import org.cosmic.backend.domain.bestAlbum.domains.UserBestAlbum;
import org.cosmic.backend.domain.playList.domains.Album;
import org.cosmic.backend.domain.playList.domains.Artist;
import org.cosmic.backend.domain.playList.domains.Track;
import org.cosmic.backend.domain.user.domains.Email;
import org.cosmic.backend.domain.user.domains.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.TestComponent;

@TestComponent
public class Creator {

  @Autowired
  private TestEntityManager testEntityManager;

  public List<User> createAndSaveUsers(int size) {
    String baseName = "username";
    return IntStream.range(0, size).mapToObj(idx -> createAndSaveUser(baseName + idx)).toList();
  }

  public User createAndSaveUser(String username) {
    return testEntityManager.persistAndFlush(createUser(username));
  }

  public AlbumChatComment createAndSaveAlbumChat(Album album, User user, String content) {
    return testEntityManager.persistAndFlush(AlbumChatComment.from(album, user, content));
  }

  public AlbumChatComment createAndSaveAlbumChatComment(AlbumChatComment albumChatComment,
      User user, String content) {
    return testEntityManager.persistAndFlush(AlbumChatComment.builder()
        .album(albumChatComment.getAlbum())
        .user(user)
        .content(content)
        .parentAlbumChatComment(albumChatComment)
        .build());
  }

  public List<AlbumChatComment> createAndSaveAlbumChatComments(AlbumChatComment albumChatComment,
      List<User> users, String content) {
    return users.stream()
        .map(user -> createAndSaveAlbumChatComment(albumChatComment, user, content))
        .toList();
  }

  public AlbumChatCommentLike createAndSaveAlbumChatLike(AlbumChatComment albumChatComment,
      User user) {
    return testEntityManager.persistAndFlush(AlbumChatCommentLike.builder()
        .user(user)
        .albumChatComment(albumChatComment)
        .build());
  }

  public List<AlbumChatCommentLike> createAndSaveAlbumChatLikes(AlbumChatComment albumChatcomment,
      List<User> users) {
    return users.stream()
        .map(user -> createAndSaveAlbumChatLike(albumChatcomment, user))
        .toList();
  }

  private User createUser(String username) {
    return User.builder()
        .email(testEntityManager.persistAndFlush(
            Email.builder().email(username + "@example.com").verificationCode("1234").build()))
        .password("1").username(username).build();
  }

  public List<UserBestAlbum> createAndSaveUserBestAlbums(User user, int size) {
    return createAndSaveUserBestAlbums(user, 0, size);
  }

  public List<UserBestAlbum> createAndSaveUserBestAlbums(User user, int start, int until) {
    List<Album> albums = createAndSaveAlbums(start, until);
    return albums.stream().map(album -> UserBestAlbum.from(user, album, 1)).toList();
  }

  public List<Album> createAndSaveAlbums(int size) {
    return createAndSaveAlbums(0, size);
  }

  public List<Track> createAndSaveTracks(int start, int until) {
    String baseName = "track";
    return IntStream.range(start, until).mapToObj(idx -> createAndSaveTracks(baseName + idx))
        .toList();
  }

  private Track createAndSaveTracks(String name) {
    Album album = createAndSaveAlbums(name);
    return testEntityManager.persistAndFlush(Track.builder()
        .trackCover(name)
        .spotifyTrackId(name)
        .title(name)
        .album(album)
        .artist(album.getArtist())
        .build());
  }


  public List<Album> createAndSaveAlbums(int start, int until) {
    String baseName = "album";
    return IntStream.range(start, until).mapToObj(idx -> createAndSaveAlbums(baseName + idx))
        .toList();
  }

  public Album createAndSaveAlbums(String baseName) {
    return testEntityManager.persistAndFlush(Album.builder()
        .artist(testEntityManager.persistAndFlush(
            Artist.builder()
                .artistCover(baseName + "Artist")
                .artistName(baseName + "Artist")
                .spotifyArtistId(baseName)
                .build()))
        .albumCover(baseName)
        .spotifyAlbumId(baseName)
        .title(baseName)
        .build()
    );
  }

  public List<AlbumChatComment> createAndSaveAlbumChats(Album album, User user, String content,
      int start, int until) {
    return IntStream.range(start, until)
        .mapToObj(idx -> createAndSaveAlbumChat(album, user, content)).toList();
  }
}
