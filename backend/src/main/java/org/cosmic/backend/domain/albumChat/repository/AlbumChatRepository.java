package org.cosmic.backend.domain.albumChat.repository;

import org.cosmic.backend.domain.albumChat.domain.AlbumChat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AlbumChatRepository extends JpaRepository<AlbumChat,Long> {
    Optional<AlbumChat> findByAlbum_AlbumId(Long albumId);//key로 찾기
}