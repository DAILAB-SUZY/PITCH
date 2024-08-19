package org.cosmic.backend.domain.playList.repository;

import org.cosmic.backend.domain.playList.domain.Track;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TrackRepository extends JpaRepository<Track,Long> {
    Optional<Track> findByTitle(String title);
    List<Track> findByArtist_ArtistId(Long artistId);
    Track findBytrackId(Long trackId);
    Optional<Track> findByTitleAndAlbum_AlbumId(String title, Long albumId);
    Optional<List<Track>>findByAlbum_AlbumIdAndArtist_ArtistId(Long albumId,Long artistId);

}