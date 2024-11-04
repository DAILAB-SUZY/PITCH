package org.cosmic.backend.domain.playList.repositorys;

import java.util.List;
import java.util.Optional;
import org.cosmic.backend.domain.playList.domains.Playlist_Track;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PlaylistTrackRepository extends JpaRepository<Playlist_Track, Long> {

  void deleteByPlaylist_PlaylistId(Long playlistId);

  Optional<List<Playlist_Track>> findByPlaylist_PlaylistId(Long playlistId);

  @Query(
      "SELECT pt " +
          "FROM Playlist_Track pt " +
          "WHERE pt.playlist.playlistId = :playlistId"
  )
  List<Playlist_Track> findTop3ByPlaylistId(@Param("playlistId") Long playlistId,
      Pageable pageable);
}
