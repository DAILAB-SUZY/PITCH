package org.cosmic.backend.domain.favoriteArtist.domains;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.user.domains.User;


@Data
@NoArgsConstructor
@Entity
@Table(name="`FavoriteArtist`")
public class FavoriteArtist {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="favoriteArtistId")
    private Long favoriteArtistId ;

    private String artistName;

    private String albumName;

    private String cover;

    private String trackName;

    @OneToOne
    @JoinColumn(name="userId")
    private User user;

    public FavoriteArtist(String artistName, String albumName,  String trackName,String cover, User user) {
        this.artistName = artistName;
        this.albumName = albumName;
        this.cover = cover;
        this.trackName = trackName;
        this.user = user;
    }

}
