package org.cosmic.backend.domain.playList.repositorys;

import org.cosmic.backend.domain.playList.domains.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface PlaylistRepository extends JpaRepository<Playlist,Long> {
    Optional<Playlist> findByUser_UserId(Long userId);
}