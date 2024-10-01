package org.cosmic.backend.domain.playList.repositorys;

import org.cosmic.backend.domain.playList.domains.Track;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrackRepository extends JpaRepository<Track,Long> {
    Optional<Track> findByTitle(String title);
    Optional<Track> findByTitleAndAlbum_AlbumId(String title, Long albumId);
    List<Track>findByAlbum_TitleAndArtist_ArtistId(String title,Long artistId);
    Optional<Track> findByTrackIdAndArtist_ArtistId(Long trackId,Long artistId);

    List<Track> findByArtist_ArtistName(String artistName);
    Optional<Track> findBySpotifyTrackId(String spotifyTrackId);
}
