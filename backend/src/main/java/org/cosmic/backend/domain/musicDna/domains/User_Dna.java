package org.cosmic.backend.domain.musicDna.domains;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.user.domains.User;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
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