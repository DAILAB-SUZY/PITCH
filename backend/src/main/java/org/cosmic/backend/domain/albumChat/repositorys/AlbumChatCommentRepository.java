package org.cosmic.backend.domain.albumChat.repositorys;

import org.cosmic.backend.domain.albumChat.domains.AlbumChatComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AlbumChatCommentRepository extends JpaRepository<AlbumChatComment,Long> {
    Optional<List<AlbumChatComment>> findByAlbumChat_AlbumChatId(Long albumChatid);//key로 찾기
    @Query("SELECT acc FROM AlbumChatComment acc LEFT JOIN acc.albumChatCommentLikes acl GROUP BY acc ORDER BY COUNT(acl) DESC")
    Optional<List<AlbumChatComment>> findByAlbumChatIdOrderByCountAlbumChatCommentLikes(Long albumChatid);//key로 찾기
    AlbumChatComment findByAlbumChatCommentId(Long albumChatCommentid);
}