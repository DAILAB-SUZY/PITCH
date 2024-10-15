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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.playList.dtos.TrackDetail;
import org.cosmic.backend.domain.search.dtos.SpotifyTrack;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "Track")
public class Track {//여기엔 모든 노래들이 담길 것임. 담길 때 앨범도 같이
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "track_id")
  private Long trackId;

  @Column(name = "spotify_track_id")
  private String spotifyTrackId;

  @OneToOne(fetch = FetchType.EAGER)
  private Genre genre;

  @Column(nullable = false)
  private String title;//곡 제목

  @OneToMany(mappedBy = "track")
  private List<Playlist_Track> playlist_track;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "album_id")
  private Album album;

  private String trackCover;

  //아티스트와 1:N관계
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "artist_id")
  private Artist artist;

  public Track(Genre genre, String title, Artist artist, Album album) {
    this.genre = genre;
    this.title = title;
    this.artist = artist;
    this.album = album;
    this.trackCover = trackCover;
  }

  public static TrackDetail toTrackDetail(Track track) {
    return TrackDetail.builder()
        .title(track.getTitle())
        .artistName(track.getArtist().getArtistName())
        .build();
  }

  public static Track from(SpotifyTrack body, Album album,
      Artist artist) {
    return Track.builder()
        .title(body.name())
        .spotifyTrackId(body.id())
        .album(album)
        .artist(artist)
        .trackCover(body.album().images().get(0).url())
        .build();
  }
}