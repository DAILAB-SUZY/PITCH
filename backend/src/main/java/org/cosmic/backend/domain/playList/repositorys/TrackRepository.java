package org.cosmic.backend.domain.playList.repositorys;

import org.cosmic.backend.domain.playList.domains.Track;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TrackRepository extends JpaRepository<Track,Long> {
    Optional<Track> findByTitle(String title);
    List<Track> findByArtist_ArtistId(Long artistId);
    Track findBytrackId(Long trackId);
    Optional<Track> findByTitleAndAlbum_AlbumId(String title, Long albumId);
    List<Track>findByAlbum_TitleAndArtist_ArtistId(String title,Long artistId);
    Optional<Track> findByTrackIdAndArtist_ArtistId(Long trackId,Long artistId);
}
