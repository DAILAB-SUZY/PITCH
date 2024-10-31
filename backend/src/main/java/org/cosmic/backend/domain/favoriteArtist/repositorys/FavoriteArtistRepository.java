package org.cosmic.backend.domain.favoriteArtist.repositorys;

import java.util.Optional;
import org.cosmic.backend.domain.favoriteArtist.domains.FavoriteArtist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteArtistRepository extends JpaRepository<FavoriteArtist, Long> {

  Optional<FavoriteArtist> findByUser_UserId(Long userId);

  void deleteByUser_UserId(Long userId);
}
