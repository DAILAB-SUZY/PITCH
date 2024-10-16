package org.cosmic.backend.domain.post.dtos.Post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.playList.domains.Album;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlbumDetail {

  private Long albumId;
  private String title;
  private String albumCover;
  private String artistName;
  private String genre;

  public static AlbumDetail from(Album album) {
    return AlbumDetail.builder()
        .albumId(album.getAlbumId())
        .title(album.getTitle())
        .albumCover(album.getAlbumCover())
        .artistName(album.getArtist().getArtistName())
        .genre(album.getGenre())
        .build();
  }
}
