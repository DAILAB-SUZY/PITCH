package org.cosmic.backend.domain.bestAlbum.dtos;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.bestAlbum.domains.UserBestAlbum;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BestAlbumDetail {

  Long albumId;
  String albumName;
  String albumCover;
  Integer score;
  String spotifyId;

  public static BestAlbumDetail from(UserBestAlbum bestAlbums) {
    return BestAlbumDetail.builder()
        .albumId(bestAlbums.getAlbum().getAlbumId())
        .albumName(bestAlbums.getAlbum().getTitle())
        .albumCover(bestAlbums.getAlbum().getAlbumCover())
        .score(bestAlbums.getScore())
        .spotifyId(bestAlbums.getAlbum().getSpotifyAlbumId())
        .build();
  }

  public static List<BestAlbumDetail> from(List<UserBestAlbum> userBestAlbums) {
    return userBestAlbums.stream().map(BestAlbumDetail::from).toList();
  }
}
