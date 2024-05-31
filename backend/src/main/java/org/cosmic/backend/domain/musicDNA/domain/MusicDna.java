package org.cosmic.backend.domain.musicDNA.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name="`DNA`")
public class MusicDna {//dna들을 담고 있는 테이블.

    //primary key id로 설정 후 user와 연결하는 테이블만들기.
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="emotionId")
    private Long emotionId;//emotion key

    @Builder.Default
    @Column()
    private String emotion="느긋한";

    @OneToMany(mappedBy = "emotion")
    private List<User_Dna> users=new ArrayList<>();

    public MusicDna(String emotion) {
        this.emotion = emotion;
    }
}

