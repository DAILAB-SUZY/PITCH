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

  public static FavoriteArtistDetail from(FavoriteArtist favoriteArtist) {
    return FavoriteArtistDetail.builder()
        .artistName(favoriteArtist.getArtist().getArtistName())
        .albumName(favoriteArtist.getAlbum().getTitle())
        .trackName(favoriteArtist.getTrack().getTitle())
        .artistCover(favoriteArtist.getArtist().getArtistCover())
        .albumCover(favoriteArtist.getAlbum().getAlbumCover())
        .trackCover(favoriteArtist.getTrack().getTrackCover())
        .build();
  }
}
