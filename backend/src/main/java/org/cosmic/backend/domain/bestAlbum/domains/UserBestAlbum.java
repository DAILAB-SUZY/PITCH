package org.cosmic.backend.domain.bestAlbum.domains;

import jakarta.persistence.Column;
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
@Table(name = "user_bestalbum")
@IdClass(UserBestAlbumPK.class)
public class UserBestAlbum {

  @Id
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "userId")
  private User user;

  @Id
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "AlbumId")
  private Album album;

  @Builder.Default
  @Column(name = "`order`")
  private Integer order = 0;

  @Builder.Default
  private Integer score = 0;

  public static UserBestAlbum from(User user, Album album, Integer score) {
    return UserBestAlbum.builder()
        .user(user)
        .album(album)
        .score(score)
        .build();
  }
}
