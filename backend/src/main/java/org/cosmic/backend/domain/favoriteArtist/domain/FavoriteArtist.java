package org.cosmic.backend.domain.favoriteArtist.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.user.domain.User;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name="`FavoriteArtist`")
public class FavoriteArtist {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="favoriteArtistId")
    private Long favoriteArtistId ;
    //USER

    private String artistName;

    private String albumName;

    private String cover;

    private String trackName;

    @OneToOne
    @JoinColumn(name="userId")
    private User user;
    //user id로 플레이리스트의 주인을 찾음
}
