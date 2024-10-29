package org.cosmic.backend.domain.albumScore.repositorys;

import org.cosmic.backend.domain.albumScore.domains.AlbumScore;
import org.cosmic.backend.domain.albumScore.domains.AlbumScorePK;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface AlbumScoreRepository extends JpaRepository<AlbumScore, AlbumScorePK> {

    Boolean existsByAlbum_AlbumIdAndUser_UserId(Long albumId, Long userId);
    AlbumScore findByAlbum_AlbumIdAndUser_UserId(Long albumId, Long userId);
    List<AlbumScore> findByUser_UserId(Long userId);
}
