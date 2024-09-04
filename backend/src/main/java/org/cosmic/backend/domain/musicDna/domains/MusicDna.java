package org.cosmic.backend.domain.musicDna.domains;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.playList.domains.Album;
import org.cosmic.backend.domain.user.domains.User;

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

    @OneToMany(mappedBy = "dna1")
    private List<User> users1;

    @OneToMany(mappedBy = "dna2")
    private List<User> users2;

    @OneToMany(mappedBy = "dna3")
    private List<User> users3;

    @OneToMany(mappedBy = "dna4")
    private List<User> users4;
}

