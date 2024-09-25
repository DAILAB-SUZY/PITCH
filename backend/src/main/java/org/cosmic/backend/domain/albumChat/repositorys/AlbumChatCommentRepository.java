package org.cosmic.backend.domain.albumChat.repositorys;

import org.cosmic.backend.domain.albumChat.domains.AlbumChatComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AlbumChatCommentRepository extends JpaRepository<AlbumChatComment,Long> {

    Optional<List<AlbumChatComment>> findByAlbumChatCommentId(Long albumChatCommentid);//key로 찾기
    Optional<List<AlbumChatComment>> findByAlbum_AlbumId(Long albumid);//key로 찾기
    @Query("SELECT acc " +
            "FROM AlbumChatComment acc " +
            "LEFT JOIN acc.albumChatCommentLikes acl " +
            "WHERE acc.album.albumId = :albumid " +
            "AND acc.parentAlbumChatCommentId IS NULL " +
            "GROUP BY acc " +
            "ORDER BY COUNT(acl) DESC " +
            "LIMIT 10 " +
            "OFFSET :count"
    )
    Optional<List<AlbumChatComment>> findByAlbumIdOrderByCountAlbumChatCommentLikes(Long albumid,int count);//key로 찾기

    @Query(
            "SELECT acc " +
            "FROM AlbumChatComment acc " +
            "WHERE acc.album.albumId=:albumid " +
            "AND acc.parentAlbumChatCommentId IS NULL " +
            "ORDER BY acc.createTime DESC" +
            " LIMIT 10 " +
            " OFFSET :count "
    )
    Optional<List<AlbumChatComment>> findByAlbumIdOrderByRecentAlbumChatCommentLikes(Long albumid,int count);

    @Query(
            "SELECT acc " +
            "FROM AlbumChatComment acc " +
            "WHERE acc.album.albumId = :albumId " +
            "AND acc.parentAlbumChatCommentId = :albumChatCommentId " +
            "ORDER BY acc.createTime DESC"
    )
    Optional<List<AlbumChatComment>> findByAlbumIdOrderByReply(@Param("albumId") Long albumId, @Param("albumChatCommentId") Long albumChatCommentId);
}