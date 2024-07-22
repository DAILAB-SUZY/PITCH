package org.cosmic.backend.domain.albumChat.repositorys;

import org.cosmic.backend.domain.albumChat.domains.AlbumChatCommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AlbumChatCommentLikeRepository extends JpaRepository<AlbumChatCommentLike,Long> {
    List<AlbumChatCommentLike> findByAlbumChatComment_AlbumChatCommentId(Long albumChatCommentId);
    Optional<AlbumChatCommentLike> findByAlbumChatComment_AlbumChatCommentIdAndUser_UserId(Long albumChatCommentId, Long userId);
    Long countByAlbumChatComment_AlbumChatCommentId(Long albumChatCommentId);
}