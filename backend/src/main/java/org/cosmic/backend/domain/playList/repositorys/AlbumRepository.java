package org.cosmic.backend.domain.playList.repositorys;

import org.cosmic.backend.domain.playList.domains.Album;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AlbumRepository extends JpaRepository<Album,Long> {
    List<Album> findAllByArtist_ArtistId(Long artistId);
    Optional<List<Album>> findAllByTitle(String title);
    Optional<Album> findByTitleAndArtist_ArtistId(String title, Long artistId);
    Optional<Album> findByTitleAndArtist_ArtistName(String title, String artistName);
    Optional<Album> findByAlbumIdAndArtist_ArtistId(Long albumId,Long artistId);

}