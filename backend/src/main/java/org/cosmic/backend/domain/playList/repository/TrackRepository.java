package org.cosmic.backend.domain.playList.repository;

import org.cosmic.backend.domain.playList.domain.Track;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrackRepository extends JpaRepository<Track,Long> {
    Track findBytrackId(Long trackId);

}
