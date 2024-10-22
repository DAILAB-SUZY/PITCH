package org.cosmic.backend.domain.playList.repositorys;

import org.cosmic.backend.domain.playList.domains.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArtistRepository extends JpaRepository<Artist,Long> {
    Optional<Artist> findByArtistName(String artistName);
    Optional<List<Artist>>findAllByArtistName(String artistName);
    Optional<Artist> findBySpotifyArtistId(String SpotifyArtistId);
}