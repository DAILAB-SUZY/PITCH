package org.cosmic.backend.domain.playList.repository;

import org.cosmic.backend.domain.playList.domain.Track;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrackRepository extends JpaRepository<Track,Long> {
    Track findByTitle(String title);
    List<Track> findByArtist_ArtistId(Long artistId);
    Track findBytrackId(Long trackId);

}
