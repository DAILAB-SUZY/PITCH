package org.cosmic.backend.domain.favoriteArtist.repositorys;

import org.cosmic.backend.domain.favoriteArtist.domains.FavoriteArtist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FavoriteArtistRepository extends JpaRepository<FavoriteArtist,Long> {
    Optional<FavoriteArtist> findByUser_UserId(Long userId);

}
