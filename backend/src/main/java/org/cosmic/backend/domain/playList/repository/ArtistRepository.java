package org.cosmic.backend.domain.playList.repository;

import org.cosmic.backend.domain.playList.domain.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArtistRepository extends JpaRepository<Artist,Long> {
    Optional<Artist> findById(Long artistId);//key로 찾기
}