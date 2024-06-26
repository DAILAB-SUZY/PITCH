package org.cosmic.backend.domain.playList.repository;

import org.cosmic.backend.domain.playList.domain.Album;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlbumRepository extends JpaRepository<Album,Long> {
    Album findByAlbumId(Long albumId);
}
