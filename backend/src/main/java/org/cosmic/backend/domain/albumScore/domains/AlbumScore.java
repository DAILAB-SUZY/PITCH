package org.cosmic.backend.domain.albumScore.domains;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.playList.domains.Album;
import org.cosmic.backend.domain.user.domains.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "album_score")
@IdClass(AlbumScorePK.class)
public class AlbumScore {

  public static final AlbumScore NONE_SCORE = AlbumScore.builder().score(0).build();
  @Id
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "userId")
  private User user;

  @Id
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "AlbumId")
  private Album album;

  @Builder.Default
  private Integer score = 0;

  public static AlbumScore from(User user, Album album, Integer score) {
    return AlbumScore.builder()
        .user(user)
        .album(album)
        .score(score)
        .build();
  }
}
