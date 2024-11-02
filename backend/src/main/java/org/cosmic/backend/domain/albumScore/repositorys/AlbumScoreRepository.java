package org.cosmic.backend.domain.albumScore.repositorys;

import io.lettuce.core.dynamic.annotation.Param;
import java.util.List;
import java.util.Optional;
import org.cosmic.backend.domain.albumScore.domains.AlbumScore;
import org.cosmic.backend.domain.albumScore.domains.AlbumScorePK;
import org.cosmic.backend.domain.playList.domains.Album;
import org.cosmic.backend.domain.user.domains.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface AlbumScoreRepository extends JpaRepository<AlbumScore, AlbumScorePK> {

  Optional<AlbumScore> findByAlbum_SpotifyAlbumIdAndUser_UserId(String spotifyAlbumId, Long userId);


  Optional<AlbumScore> findByAlbum_AlbumIdAndUser_UserId(Long albumId, Long userId);

  List<AlbumScore> findByUser_UserId(Long userId);

  Optional<AlbumScore> findByAlbumAndUser(Album album, User user);

  void deleteByAlbum_AlbumIdAndUser_UserId(Long albumId, Long userId);

  @Query(
      "SELECT new org.cosmic.backend.domain.albumScore.domains.AlbumScore("
          + "a.user,"
          + "al, "
          + "COALESCE(a.score, 0)) "
          + "FROM Album al "
          + "LEFT JOIN AlbumScore a ON a.album = al and a.user.userId = :userId "
          + "LEFT JOIN a.album.albumChatComments acc "
          + "GROUP BY al.albumId, a.user, a.album.albumId, a.score "
          + "ORDER BY COUNT(acc) DESC, al.albumId")
  Page<AlbumScore> findAlbumScoreOrderByCommentCount(Pageable pageable,
      @Param("userId") Long userId);

  @Query(
      "SELECT new org.cosmic.backend.domain.albumScore.domains.AlbumScore("
          + "a.user,"
          + "al, "
          + "COALESCE(a.score, 0)) "
          + "FROM Album al "
          + "LEFT JOIN AlbumScore a ON a.album = al and a.user.userId = :userId "
          + "LEFT JOIN a.album.albumLike all "
          + "GROUP BY al.albumId, a.user, a.album.albumId, a.score "
          + "ORDER BY COUNT(all.user) DESC, al.albumId")
  Page<AlbumScore> findAlbumScoreOrderByAlbumLikeCount(Pageable pageable,
      @Param("userId") Long userId);
}
