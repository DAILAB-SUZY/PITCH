package org.cosmic.backend.domain.albumChat.repositorys;

import org.cosmic.backend.domain.albumChat.domains.AlbumChatComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AlbumChatCommentRepository extends JpaRepository<AlbumChatComment,Long> {
    Optional<List<AlbumChatComment>> findByAlbum_AlbumId(Long albumid);//key로 찾기
    @Query("SELECT acc " +
            "FROM AlbumChatComment acc " +
            "LEFT JOIN acc.albumChatCommentLikes acl " +
            "WHERE acc.album.albumId = :albumid " +
            "GROUP BY acc " +
            "ORDER BY COUNT(acl) DESC")
    Optional<List<AlbumChatComment>> findByAlbumIdOrderByCountAlbumChatCommentLikes(Long albumid);//key로 찾기
}