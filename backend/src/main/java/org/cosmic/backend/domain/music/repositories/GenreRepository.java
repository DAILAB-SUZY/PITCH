package org.cosmic.backend.domain.music.repositories;

import org.cosmic.backend.domain.music.domain.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GenreRepository extends JpaRepository<Genre, Long> {
    Optional<Genre> findByName(final String name);
}
