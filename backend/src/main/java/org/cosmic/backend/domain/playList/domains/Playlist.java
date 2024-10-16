package org.cosmic.backend.domain.playList.domains;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.user.domains.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "playlist")
@EqualsAndHashCode(exclude = {"user", "playlist_track"})
public class Playlist {//트랙은 플레이리스트는 N:M관계임
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "playlist_id")
  private Long playlistId;

  @Builder.Default
  @Column(nullable = false, name = "update_time")
  private Instant updateTime = Instant.now();//최신 업데이트 날짜

  @OneToOne
  @JoinColumn(name = "user_id")
  private User user;
  //user id로 플레이리스트의 주인을 찾음

  @OneToMany(mappedBy = "playlist", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private List<Playlist_Track> playlist_track = new ArrayList<>();

  public List<String> getSpotifyTrackIds() {
    return getPlaylist_track().stream().map(Playlist_Track::getTrackId).toList();
  }
}