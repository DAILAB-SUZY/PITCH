package org.cosmic.backend.domain.playList.repository;

import org.cosmic.backend.domain.playList.domain.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistRepository extends JpaRepository<Artist,Long> {
    Artist findByArtistName(String artistName);
}