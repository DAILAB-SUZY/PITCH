package org.cosmic.backend.domain.music.repositories;

import org.cosmic.backend.domain.music.domain.Tracks;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TracksRepository extends JpaRepository<Tracks, Long> {

}
