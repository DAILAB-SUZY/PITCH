package org.cosmic.backend.domain.favoriteArtist.domains;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.favoriteArtist.dtos.FavoriteArtistDetail;
import org.cosmic.backend.domain.playList.domains.Album;
import org.cosmic.backend.domain.playList.domains.Artist;
import org.cosmic.backend.domain.playList.domains.Track;
import org.cosmic.backend.domain.user.domains.User;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name="`FavoriteArtist`")
@IdClass(FavoriteArtistPK.class)
public class FavoriteArtist {

    @Id
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_id")
    private Artist artist;

    @Id
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Track track;

    @Builder.Default
    @ManyToOne(fetch = FetchType.LAZY)
    private Album album = new Album(); // @Builder.Default 추가

    public static FavoriteArtistDetail toFavoriteArtistDto(FavoriteArtist favoriteArtist){
        return FavoriteArtistDetail.builder()
                .artistName(favoriteArtist.artist.getArtistName())
                .albumCover(favoriteArtist.album.getAlbumCover())
                .trackCover(favoriteArtist.track.getTrackCover())
                .artistCover(favoriteArtist.artist.getArtistCover())
                .albumName(favoriteArtist.track.getAlbum().getTitle())
                .trackName(favoriteArtist.track.getTitle())
                .build();
    }
}
