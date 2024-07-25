package org.cosmic.backend.domain.musicDna.repositorys;
import org.cosmic.backend.domain.musicDna.domains.MusicDna;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface EmotionRepository extends JpaRepository<MusicDna,Long> {
    Optional<MusicDna> findByEmotionId(Long emotionId);//key로 찾기
}
