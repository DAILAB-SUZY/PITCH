package org.cosmic.backend.domain.albumChat.domains;

import jakarta.persistence.CascadeType;
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
import java.util.List;
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
@Table(name = "albumChatComment") // 테이블 이름 수정
@Builder
public class AlbumChatComment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "albumChatComment_id") // 컬럼 이름 명시
  private Long albumChatCommentId;

  @ManyToOne
  @JoinColumn(name = "album_id")
  private Album album;

  private String content;

  @ManyToOne(fetch = FetchType.LAZY)
  private AlbumChatComment parentAlbumChatComment;

  @Column(name = "create_time")
  @Builder.Default
  private Instant createTime = Instant.now();

  @Column(name = "update_time")
  private Instant updateTime;

  @Builder.Default
  @OneToMany(mappedBy = "albumChatComment", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<AlbumChatCommentLike> albumChatCommentLikes = new ArrayList<>();

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  public static AlbumChatComment from(Album album, User user, String content) {
    return AlbumChatComment.builder()
        .album(album)
        .content(content)
        .user(user)
        .build();
  }

  @Override
  public String toString() {
    return "AlbumChatComment{" +
        "albumChatCommentId=" + albumChatCommentId +
        ", content='" + content + '\'' +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        ", user=" + (user != null ? user.getUserId() : "null") +
        '}';
  }
}
