package org.cosmic.backend.domain.playList.domains;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.albumChat.domains.AlbumChatComment;
import org.cosmic.backend.domain.albumChat.domains.AlbumLike;
import org.cosmic.backend.domain.albumChat.dtos.albumChat.AlbumChatDetail;
import org.cosmic.backend.domain.post.dtos.Post.AlbumDetail;
import org.cosmic.backend.domain.post.dtos.Post.AlbumDto;
import org.cosmic.backend.domain.post.entities.Post;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "Album")
@EqualsAndHashCode
public class Album {//앨범과 트랙은 1:N관계이며 앨범과 아티스트는 더 생각 필요

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "album_id")
  private Long albumId;

  @Column(name = "spotify_album_id")
  private String spotifyAlbumId;

  @Column(nullable = false)
  private String title;//앨범 제목

  @Column(nullable = false)
  private String albumCover;
  /*
    @Builder.Default
    @ManyToMany(fetch = FetchType.LAZY)*/
  private String genre = "balad";

  @Builder.Default
  @Column(nullable = false)
  private Instant createdDate = Instant.now();//발매 일
  //TODO 애초에 저장할 때 STRING으로 2024-04-05형식으로 넣기.
  @OneToMany(mappedBy = "album")
  @Builder.Default
  private Set<Post> posts = new HashSet<>();

  //아티스트와 1:N 관계
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "artist_id")
  private Artist artist;

  @OneToMany(mappedBy = "album")
  @Builder.Default
  private List<Track> track = new ArrayList<>();

  @OneToMany(mappedBy = "album")
  private List<AlbumChatComment> albumChatComments;

  @OneToMany(mappedBy = "album")
  @Builder.Default
  private List<AlbumLike> albumLike = new ArrayList<>();

  public static AlbumDetail toAlbumDetail(Album album) {
    return AlbumDetail.builder()
        .id(album.getAlbumId())
        .title(album.getTitle())
        .albumCover(album.getAlbumCover())
        .artistName(album.getArtist().getArtistName())
        .genre(album.genre)
        .build();
  }

  public static List<AlbumDetail> toAlbumDetail(List<Album> album) {
    List<AlbumDetail> albumDetails = new ArrayList<>();
    album.forEach(albumDetail -> albumDetails.add(toAlbumDetail(albumDetail)));
    return albumDetails;
  }

  public static AlbumChatDetail toAlbumChatDetail(Album album) {
    return AlbumChatDetail.builder()
        .albumId(album.getAlbumId())
        .title(album.getTitle())
        .cover(album.getAlbumCover())
        .genre(album.genre.toString())
        .artistName(album.getArtist().getArtistName())
        .comments(
            album.getAlbumChatComments().stream().map(AlbumChatComment::toAlbumChatCommentDetail)
                .toList())
        .albumLike(
            album.getAlbumLike().stream().map(AlbumLike::toAlbumChatAlbumLikeDetail).toList())
        .build();
  }

  public static AlbumDto toAlbumDto(Album album) {
    return AlbumDto.builder()
        .albumId(album.getAlbumId())
        .albumName(album.getTitle())
        .artistName(album.getArtist().getArtistName())
        .build();
  }

  public static Album from(org.cosmic.backend.domain.search.dtos.Album album, Artist artist) {
    return Album.builder()
        .title(album.name())
        .albumCover(album.images().get(0).url())
        .spotifyAlbumId(album.id())
        .artist(artist)
        .build();
  }

  @Override
  public String toString() {
    return "Album{" +
        "albumId=" + albumId +
        ", title='" + title + '\'' +
        ", albumCover='" + albumCover + '\'' +
        ", createdDate=" + createdDate +
        ", artist=" + (artist != null ? artist.getArtistId() : "null") +
        '}';
  }
}