package org.cosmic.backend.domain.favoriteArtist.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.favoriteArtist.domains.FavoriteArtist;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteArtistDetail {

  private String artistName;
  private String albumName;
  private String trackName;
  private String artistCover;
  private String albumCover;
  private String trackCover;
  private String spotifyArtistId;
  private String spotifyAlbumId;
  private String spotifyTrackId;

  public static FavoriteArtistDetail from(FavoriteArtist favoriteArtist) {
    if (favoriteArtist.isNotSet()) {
      return FavoriteArtistDetail.builder()
          .artistName("없음")
          .albumCover("없음")
          .trackCover("없음")
          .artistCover("없음")
          .albumName("없음")
          .trackName("없음")
          .spotifyArtistId("Not Found")
          .spotifyTrackId("Not Found")
          .spotifyAlbumId("Not Found")
          .build();
    }
    return FavoriteArtistDetail.builder()
        .artistName(favoriteArtist.getArtist().getArtistName())
        .albumName(favoriteArtist.getAlbum().getTitle())
        .trackName(favoriteArtist.getTrack().getTitle())
        .artistCover(favoriteArtist.getArtist().getArtistCover())
        .albumCover(favoriteArtist.getAlbum().getAlbumCover())
        .trackCover(favoriteArtist.getTrack().getTrackCover())
        .spotifyArtistId(favoriteArtist.getSpotifyArtistId())
        .spotifyAlbumId(favoriteArtist.getSpotifyAlbumId())
        .spotifyTrackId(favoriteArtist.getSpotifyTrackId())
        .build();
  }
}
