package org.cosmic.backend.domain.playList.repository;

import org.cosmic.backend.domain.playList.domain.Album;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AlbumRepository extends JpaRepository<Album,Long> {
    Optional<Album> findByAlbumId(Long albumId);
    void deleteByAlbumId(Long albumId);
}
