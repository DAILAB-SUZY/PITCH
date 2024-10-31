package org.cosmic.backend.domain.albumChat.repositorys;

import java.util.List;
import java.util.Optional;
import org.cosmic.backend.domain.albumChat.domains.AlbumChatComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AlbumChatCommentRepository extends JpaRepository<AlbumChatComment, Long> {

  Optional<List<AlbumChatComment>> findByAlbum_AlbumId(Long albumid);//key로 찾기

  @EntityGraph(attributePaths = {"childComments"})
  List<AlbumChatComment> findAllByAlbum_AlbumIdAndParentAlbumChatCommentIsNull(Long albumId);

  @Query("SELECT acc " +
      "FROM AlbumChatComment acc " +
      "LEFT JOIN acc.albumChatCommentLikes acl " +
      "WHERE acc.album.albumId = :albumid " +
      "AND acc.parentAlbumChatComment IS NULL " +
      "GROUP BY acc " +
      "ORDER BY COUNT(acl) DESC " +
      "LIMIT 10 " +
      "OFFSET :count"
  )
  Optional<List<AlbumChatComment>> findByAlbumIdOrderByCountAlbumChatCommentLikes(Long albumid,
      int count);//key로 찾기

  @Query(
      "SELECT acc " +
          "FROM AlbumChatComment acc " +
          "WHERE acc.album.albumId=:albumid " +
          "AND acc.parentAlbumChatComment IS NULL " +
          "ORDER BY acc.createTime DESC" +
          " LIMIT 10 " +
          " OFFSET :count "
  )
  Optional<List<AlbumChatComment>> findByAlbumIdOrderByRecentAlbumChatCommentLikes(Long albumid,
      int count);

  @Query(
      "SELECT acc " +
          "FROM AlbumChatComment acc " +
          "WHERE acc.album.albumId = :albumId " +
          "AND acc.parentAlbumChatComment = :albumChatCommentId " +
          "ORDER BY acc.createTime DESC"
  )
  Optional<List<AlbumChatComment>> findByAlbumIdOrderByReply(@Param("albumId") Long albumId,
      @Param("albumChatCommentId") Long albumChatCommentId);

  @Query("SELECT acc FROM AlbumChatComment acc WHERE acc.parentAlbumChatComment IS NULL AND acc.album.spotifyAlbumId = :spotifyAlbumId ORDER BY COALESCE(acc.updateTime, acc.createTime) DESC")
  Page<AlbumChatComment> findByAlbum_SpotifyAlbumIdOrderByTime(String spotifyAlbumId,
      Pageable pageable);

  Optional<List<AlbumChatComment>> findByUser_UserId(Long userId);

  void deleteAllByParentAlbumChatComment_AlbumChatCommentId(Long parentAlbumChatCommentId);

  @Query("SELECT acc FROM AlbumChatComment acc LEFT JOIN acc.albumChatCommentLikes acl WHERE acc.parentAlbumChatComment IS NULL AND acc.album.spotifyAlbumId = :spotifyAlbumId ORDER BY COUNT(acl) DESC")
  Page<AlbumChatComment> findByAlbum_SpotifyAlbumIdOrderByLike(String spotifyAlbumId,
      Pageable pageable);
}