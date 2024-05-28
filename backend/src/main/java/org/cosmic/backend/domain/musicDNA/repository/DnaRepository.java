package org.cosmic.backend.domain.musicDNA.repository;

import org.cosmic.backend.domain.musicDNA.domain.User_Dna;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DnaRepository extends JpaRepository<User_Dna,Long> {
    Optional<User_Dna> findById(Long id);//key로 찾기
    List<User_Dna> findAllByUserId(Long userId);
}
