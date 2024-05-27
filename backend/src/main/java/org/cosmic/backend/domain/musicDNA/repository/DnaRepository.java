package org.cosmic.backend.domain.musicDNA.repository;

import org.cosmic.backend.domain.musicDNA.domain.MusicDna;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DnaRepository extends JpaRepository<MusicDna,String> {

}
