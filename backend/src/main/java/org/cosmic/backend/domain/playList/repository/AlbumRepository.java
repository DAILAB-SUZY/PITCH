package org.cosmic.backend.domain.playList.repository;

import org.cosmic.backend.domain.playList.domain.Album;
import org.cosmic.backend.domain.playList.domain.Track;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AlbumRepository extends JpaRepository<Album,Long> {
    Optional<Album> findByAlbumId(Long albumId);
    void deleteByAlbumId(Long albumId);
    List<Album> findByArtist_ArtistId(Long artistId);
    Optional<List<Album>> findByTitle(String title);
}
