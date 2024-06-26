package org.cosmic.backend.domain.playList.repository;

import org.cosmic.backend.domain.playList.domain.Album_Track;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AlbumTrackRepository extends JpaRepository<Album_Track,Long> {

    Optional<Album_Track> findById(Long AlbumId);//key로 찾기
    List<Album_Track> findAllById(Long AlbumId);
}
