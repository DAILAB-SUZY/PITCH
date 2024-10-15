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

  public BestAlbumDetail(UserBestAlbum userBestAlbum) {
    this.albumId = userBestAlbum.getAlbum().getAlbumId();
    this.albumName = userBestAlbum.getAlbum().getTitle();
    this.albumCover = userBestAlbum.getAlbum().getAlbumCover();
    this.score = userBestAlbum.getScore();
  }

  public static BestAlbumDetail from(UserBestAlbum bestAlbums) {
    return BestAlbumDetail.builder()
        .albumId(bestAlbums.getAlbum().getAlbumId())
        .albumName(bestAlbums.getAlbum().getTitle())
        .albumCover(bestAlbums.getAlbum().getAlbumCover())
        .score(bestAlbums.getScore())
        .build();
  }

  public static List<BestAlbumDetail> from(List<UserBestAlbum> userBestAlbums) {
    return userBestAlbums.stream().map(BestAlbumDetail::from).toList();
  }
}
