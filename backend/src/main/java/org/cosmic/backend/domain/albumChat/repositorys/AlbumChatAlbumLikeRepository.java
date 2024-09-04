package org.cosmic.backend.domain.albumChat.repositorys;

import org.cosmic.backend.domain.albumChat.domains.AlbumLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AlbumChatAlbumLikeRepository extends JpaRepository<AlbumLike,Long> {
    List<AlbumLike> findByAlbumChat_AlbumChatId(Long albumChatId);
    Optional<AlbumLike> findByAlbumChat_AlbumChatIdAndUser_UserId(Long albumChatId, Long userId);
}