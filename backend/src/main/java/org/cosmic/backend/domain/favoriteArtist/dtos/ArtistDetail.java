package org.cosmic.backend.domain.favoriteArtist.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.playList.domains.Artist;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArtistDetail {

  //해당 이름 가진 아티스트 정보들을 줌
  private Long artistId;
  private String spotifyArtistId;
  private String artistCover;
  private String artistName;

  public ArtistDetail(Artist artist) {
    this.artistId = artist.getArtistId();
    this.artistCover = artist.getArtistCover();
    this.artistName = artist.getArtistName();
    this.spotifyArtistId = artist.getSpotifyArtistId();
  }
}

