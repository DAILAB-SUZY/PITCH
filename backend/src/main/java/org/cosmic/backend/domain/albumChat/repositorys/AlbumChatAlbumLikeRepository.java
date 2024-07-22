package org.cosmic.backend.domain.albumChat.repositorys;

import org.cosmic.backend.domain.albumChat.domains.AlbumChatAlbumLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AlbumChatAlbumLikeRepository extends JpaRepository<AlbumChatAlbumLike,Long> {
    List<AlbumChatAlbumLike> findByAlbumChat_AlbumChatId(Long albumChatId);
    Optional<AlbumChatAlbumLike> findByAlbumChat_AlbumChatIdAndUser_UserId(Long albumChatId, Long userId);
}