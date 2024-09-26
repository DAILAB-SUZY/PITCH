package org.cosmic.backend.domain.playList.repositorys;

import org.cosmic.backend.domain.playList.domains.Playlist;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface PlaylistRepository extends JpaRepository<Playlist,Long> {
    Optional<Playlist> findByUser_UserId(Long userId);
/*
    @Query("SELECT p FROM Playlist p WHERE p.user.userId IN :userIds AND p.updateTime >= :timeBoundary ORDER BY p.updateTime DESC")
    List<Playlist> findRecentPlaylistsByUsers(
            @Param("userIds") List<Long> userIds,
            @Param("timeBoundary") LocalDateTime timeBoundary,
            Pageable pageable
    );*/
}