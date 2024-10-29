package org.cosmic.backend.domain.albumChat.dtos.comment;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.albumChat.domains.AlbumChatComment;
import org.cosmic.backend.domain.albumChat.domains.AlbumChatCommentLike;
import org.cosmic.backend.domain.user.domains.User;
import org.cosmic.backend.domain.user.dtos.UserDetail;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlbumChatCommentDetail implements Serializable {

  private Long albumChatCommentId;
  private String content;
  private Instant createAt;
  private Instant updateAt;
  private List<UserDetail> likes;
  private List<AlbumChatReplyDetail> comments;
  private UserDetail author;

  public AlbumChatCommentDetail(AlbumChatComment albumChatComment) {
    this.albumChatCommentId = albumChatComment.getAlbumChatCommentId();
    this.content = albumChatComment.getContent();
    this.createAt = albumChatComment.getCreateTime();
    this.updateAt = albumChatComment.getUpdateTime();
    this.likes = albumChatComment.getAlbumChatCommentLikes().stream()
        .map(a -> UserDetail.from(a.getUser())).toList();
    this.comments=AlbumChatReplyDetail.from(albumChatComment.getChildComments());
    this.author = User.toUserDetail(albumChatComment.getUser());
  }

  public static List<AlbumChatCommentDetail> from(List<AlbumChatComment> albumChatComments) {
    return albumChatComments.stream().map(AlbumChatCommentDetail::from).toList();
  }

  public static AlbumChatCommentDetail from(AlbumChatComment albumChatComment) {
    return AlbumChatCommentDetail.builder()
        .albumChatCommentId(albumChatComment.getAlbumChatCommentId())
        .content(albumChatComment.getContent())
        .createAt(albumChatComment.getCreateTime())
        .updateAt(albumChatComment.getUpdateTime())
        .likes(UserDetail.from(
            albumChatComment.getAlbumChatCommentLikes().stream().map(AlbumChatCommentLike::getUser)
                .toList()))
        .comments(AlbumChatReplyDetail.from(albumChatComment.getChildComments()))
        .author(UserDetail.from(albumChatComment.getUser()))
        .build();
  }
}
