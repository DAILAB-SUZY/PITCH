package org.cosmic.backend.domain.playList.repository;

import org.cosmic.backend.domain.playList.domain.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import java.util.List;
import java.util.Optional;

public interface ArtistRepository extends JpaRepository<Artist,Long> {
    Optional<Artist> findByArtistName(String artistName);
    Optional<List<Artist>>findAllByArtistName(String artistName);
}