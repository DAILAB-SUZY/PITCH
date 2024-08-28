package org.cosmic.backend.domain.musicDna.repositorys;

import org.cosmic.backend.domain.musicDna.domains.User_Dna;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DnaRepository extends JpaRepository<User_Dna, Long> {
    List<User_Dna> findAllByUser_UserId(Long userId);
}
