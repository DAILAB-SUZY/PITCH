package org.cosmic.backend.domain.playList.repositorys;

import java.util.List;
import java.util.Optional;
import org.cosmic.backend.domain.playList.domains.Track;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TrackRepository extends JpaRepository<Track, Long> {
  
  Optional<Track> findBySpotifyTrackIdAndArtist_SpotifyArtistId(String spotifyTrackId, String spotifyArtistId);

  Optional<Track> findByTitle(String title);

  Optional<Track> findBySpotifyTrackId(String spotifyTrackId);

  @Query(value = "SELECT * FROM track ORDER BY RANDOM() LIMIT ?1", nativeQuery = true)
  List<Track> findRandomTracks(Integer cnt);
}
