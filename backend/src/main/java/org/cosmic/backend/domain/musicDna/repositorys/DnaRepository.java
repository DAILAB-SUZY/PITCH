package org.cosmic.backend.domain.musicDna.repositorys;

import org.cosmic.backend.domain.musicDna.domains.User_Dna;
import org.cosmic.backend.domain.user.domains.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DnaRepository extends JpaRepository<User_Dna, User> {
    Optional<User_Dna> findByUser(User user);//key로 찾기
    void deleteByUser_UserId(Long userId);
    Optional<List<User_Dna>> findByUser_UserId(Long userId);
}
