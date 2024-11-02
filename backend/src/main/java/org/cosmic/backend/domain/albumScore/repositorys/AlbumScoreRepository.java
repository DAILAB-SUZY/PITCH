package org.cosmic.backend.domain.albumScore.repositorys;

import java.util.List;
import java.util.Optional;
import org.cosmic.backend.domain.albumScore.domains.AlbumScore;
import org.cosmic.backend.domain.albumScore.domains.AlbumScorePK;
import org.cosmic.backend.domain.playList.domains.Album;
import org.cosmic.backend.domain.user.domains.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AlbumScoreRepository extends JpaRepository<AlbumScore, AlbumScorePK> {

  Optional<AlbumScore> findByAlbum_AlbumIdAndUser_UserId(Long albumId, Long userId);

  List<AlbumScore> findByUser_UserId(Long userId);

  Optional<AlbumScore> findByAlbumAndUser(Album album, User user);

  void deleteByAlbum_AlbumIdAndUser_UserId(Long albumId, Long userId);
}
