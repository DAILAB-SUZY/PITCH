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
import org.cosmic.backend.domain.post.entities.Post;
import org.cosmic.backend.domain.search.dtos.SpotifyAlbum;

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

  @Builder.Default
  //@ManyToMany(fetch = FetchType.LAZY)*/
  private String genre = "balad";

  @Builder.Default
  @Column(nullable = false)
  private Instant createdDate = Instant.now();//발매 일

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

  public static Album from(SpotifyAlbum spotifyAlbum, Artist artist) {
    return Album.builder()
        .title(spotifyAlbum.name())
        .albumCover(spotifyAlbum.images().get(0).url())
        .spotifyAlbumId(spotifyAlbum.id())
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