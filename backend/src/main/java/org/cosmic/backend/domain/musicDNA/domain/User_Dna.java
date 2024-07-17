package org.cosmic.backend.domain.musicDNA.domain;

import jakarta.persistence.*;
import lombok.Data;
import org.cosmic.backend.domain.user.domain.User;

@Entity
@Data
public class User_Dna {//user와 dna를 연결하는 domain

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name="emotion_id")
    private MusicDna emotion;
    @Override
    public String toString() {
        return "User_Dna{" +
                "id=" + id +
                ", emotion=" + (emotion != null ? emotion.getEmotionId() : "null") +
                '}';
    }
}