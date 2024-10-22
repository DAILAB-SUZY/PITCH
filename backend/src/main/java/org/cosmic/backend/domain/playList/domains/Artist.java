package org.cosmic.backend.domain.playList.domains;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.favoriteArtist.domains.FavoriteArtist;
import org.cosmic.backend.domain.search.dtos.SpotifyArtist;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "Artist")
public class Artist {//
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "artist_id")
  private Long artistId;

  @Column(name = "spotify_artist_id", unique = true)
  private String spotifyArtistId;

  @Column(name = "artist_name")
  private String artistName;

  private String artistCover;

  //아티스트 1 앨범 N
  @OneToMany(mappedBy = "artist")
  @Builder.Default
  private List<Album> album = new ArrayList<>();

  @OneToOne(mappedBy = "artist")
  private FavoriteArtist favoriteArtist;

  //아티스트 1 트랙 N
  @OneToMany(mappedBy = "artist")
  @Builder.Default
  private List<Track> track = new ArrayList<>();

  public Artist(String name) {
    this.artistName = name;
  }

  public static Artist from(SpotifyArtist spotifyArtist) {
    return Artist.builder()
        .artistName(spotifyArtist.name())
        .artistCover(spotifyArtist.images().get(0).url())
        .spotifyArtistId(spotifyArtist.id())
        .build();
  }

  @Override
  public String toString() {
    return "Artist{" +
        "artistId=" + artistId +
        ", artistName='" + artistName + '\'' +
        ", artistCover='" + artistCover + '\'' +
        '}';
  }
}