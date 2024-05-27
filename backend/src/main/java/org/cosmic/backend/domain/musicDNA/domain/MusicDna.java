package org.cosmic.backend.domain.musicDNA.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name="`DNA`")
public class MusicDna {//dna들을 담고 있는 테이블.
    @Id
    @Builder.Default
    @Column
    private String Emotion="느긋한";
}

