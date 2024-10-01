package org.cosmic.backend.domain.musicDna.repositorys;

import org.cosmic.backend.domain.musicDna.domains.MusicDna;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MusicDnaRepository extends JpaRepository<MusicDna,Long> {
}
