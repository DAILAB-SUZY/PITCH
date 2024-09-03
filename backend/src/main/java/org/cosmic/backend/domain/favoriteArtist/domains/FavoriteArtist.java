package org.cosmic.backend.domain.favoriteArtist.domains;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.favoriteArtist.dtos.FavoriteArtistDto;
import org.cosmic.backend.domain.playList.domains.Artist;
import org.cosmic.backend.domain.playList.domains.Track;
import org.cosmic.backend.domain.user.domains.User;


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
    private Long favoriteArtistId;

    @ManyToOne(fetch = FetchType.LAZY)
    private Track track;

    @ManyToOne(fetch = FetchType.LAZY)
    private Artist artist;

    @OneToOne
    @JoinColumn(name="userId")
    private User user;

    public static FavoriteArtistDto toFavoriteArtistDto(FavoriteArtist favoriteArtist){
        return FavoriteArtistDto.builder()
                .artistName(favoriteArtist.artist.getArtistName())
                .cover(favoriteArtist.track.getAlbum().getCover())
                .albumName(favoriteArtist.track.getAlbum().getTitle())
                .trackName(favoriteArtist.track.getTitle())
                .build();
    }
}
