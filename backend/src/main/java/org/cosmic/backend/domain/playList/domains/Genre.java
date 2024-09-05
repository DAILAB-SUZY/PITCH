package org.cosmic.backend.domain.playList.domains;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Genre {
    //TODO: 장르 기본 SQL로 설정하던지 하는 게 좋을 듯
    @Id
    private String name;
}
