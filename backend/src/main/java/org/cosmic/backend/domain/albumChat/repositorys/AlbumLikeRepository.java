package org.cosmic.backend.domain.albumChat.repositorys;

import org.cosmic.backend.domain.albumChat.domains.AlbumLike;
import org.cosmic.backend.domain.albumChat.domains.AlbumLikePK;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AlbumLikeRepository extends JpaRepository<AlbumLike, AlbumLikePK> {
    List<AlbumLike> findByAlbum_AlbumId(Long albumChatId);
    Optional<AlbumLike> findByAlbum_AlbumIdAndUser_UserId(Long albumChatId, Long userId);
    void deleteByAlbum_AlbumIdAndUser_UserId(Long albumId, Long userId);
}