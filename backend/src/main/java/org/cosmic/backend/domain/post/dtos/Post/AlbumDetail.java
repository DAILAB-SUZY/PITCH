package org.cosmic.backend.domain.post.dtos.Post;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.albumChat.domains.AlbumLike;
import org.cosmic.backend.domain.albumScore.domains.AlbumScore;
import org.cosmic.backend.domain.playList.domains.Album;
import org.cosmic.backend.domain.user.dtos.UserDetail;

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
  private String spotifyId;
  private List<UserDetail> likes;
  private Integer score;


  private static AlbumDetail from(Album album) {
    return AlbumDetail.builder()
        .albumId(album.getAlbumId())
        .title(album.getTitle())
        .albumCover(album.getAlbumCover())
        .artistName(album.getArtist().getArtistName())
        .genre(album.getGenre())
        .spotifyId(album.getSpotifyAlbumId())
        .likes(UserDetail.from(album.getAlbumLike().stream().map(AlbumLike::getUser).toList()))
        .build();
  }

  public static AlbumDetail from(Album album, Integer score) {
    AlbumDetail albumDetail = AlbumDetail.from(album);
    albumDetail.setScore(score);
    return albumDetail;
  }

  public static AlbumDetail from(AlbumScore albumScore) {
    return from(albumScore.getAlbum(), albumScore.getScore());
  }

  public static List<AlbumDetail> from(List<Album> albums) {
    return albums.stream().map(AlbumDetail::from).toList();
  }

  public static List<AlbumDetail> fromByAlbumScore(List<AlbumScore> albumScores) {
    return albumScores.stream().map(AlbumDetail::from).toList();
  }
}
