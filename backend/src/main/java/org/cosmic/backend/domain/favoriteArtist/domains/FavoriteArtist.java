package org.cosmic.backend.domain.favoriteArtist.domains;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
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
@Table(name = "`FavoriteArtist`")
public class FavoriteArtist {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "artist_id")
  private Artist artist;

  @OneToOne
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne
  private Track track;

  @ManyToOne
  private Album album;

  public static FavoriteArtistDetail toFavoriteArtistDto(FavoriteArtist favoriteArtist) {
    return FavoriteArtistDetail.builder()
        .artistName(favoriteArtist.artist.getArtistName())
        .albumCover(favoriteArtist.album.getAlbumCover())
        .trackCover(favoriteArtist.track.getTrackCover())
        .artistCover(favoriteArtist.artist.getArtistCover())
        .albumName(favoriteArtist.track.getAlbum().getTitle())
        .trackName(favoriteArtist.track.getTitle())
        .spotifyArtistId(favoriteArtist.artist.getSpotifyArtistId())
        .build();
  }

  public static FavoriteArtist from(User user) {
    return FavoriteArtist.builder()
        .user(user)
        .build();
  }

  public String getSpotifyArtistId() {
    return getArtist().getSpotifyArtistId();
  }

  public String getSpotifyAlbumId() {
    return getAlbum().getSpotifyAlbumId();
  }

  public String getSpotifyTrackId() {
    return getTrack().getSpotifyTrackId();
  }

  public boolean isNotSet() {
    return getArtist() == null && getTrack() == null && getAlbum() == null;
  }
}
