package org.cosmic.backend.domain.albumChat.domains;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.albumChat.dtos.commentlike.AlbumChatCommentLikeDetail;
import org.cosmic.backend.domain.user.domains.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "`albumChatCommentLike`")
@Builder
@IdClass(AlbumChatCommentLikePK.class)
public class AlbumChatCommentLike {

  @Id
  @ManyToOne
  @JoinColumn(name = "albumChatComment_id")
  private AlbumChatComment albumChatComment;

  @Id
  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @Column(name = "update_time")
  private Instant updateTime;

  public static AlbumChatCommentLikeDetail toAlbumChatCommentLikeDetail(
      AlbumChatCommentLike albumChatCommentLike) {
    return AlbumChatCommentLikeDetail.builder()
        .author(User.toUserDetail(albumChatCommentLike.user))
        .updateAt(albumChatCommentLike.updateTime)
        .build();
  }

  public static AlbumChatCommentLike from(AlbumChatComment albumChatComment, User user) {
    return AlbumChatCommentLike.builder()
        .user(user)
        .albumChatComment(albumChatComment)
        .build();
  }
}
