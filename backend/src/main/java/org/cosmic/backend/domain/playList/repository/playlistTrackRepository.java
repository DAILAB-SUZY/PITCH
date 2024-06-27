package org.cosmic.backend.domain.playList.repository;

import org.cosmic.backend.domain.playList.domain.Playlist_Track;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface playlistTrackRepository extends JpaRepository<Playlist_Track,Long> {
    void deleteByPlaylist_PlaylistId(Long playlistId);
    Optional<List<Playlist_Track>> findByPlaylist_PlaylistId(Long playlistId);
}
