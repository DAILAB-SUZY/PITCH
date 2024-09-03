package org.cosmic.backend.domain.musicDna.domains;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name="music_Dna")
public class MusicDna {//dna들을 담고 있는 테이블.
    //primary key id로 설정 후 user와 연결하는 테이블만들기.
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="dna_id")
    private Long dnaId;//emotion key

    @Builder.Default
    @Column(name="name")
    private String name ="느긋한";

    @OneToMany(mappedBy = "emotion",fetch = FetchType.EAGER)
    private List<User_Dna> users;
}

