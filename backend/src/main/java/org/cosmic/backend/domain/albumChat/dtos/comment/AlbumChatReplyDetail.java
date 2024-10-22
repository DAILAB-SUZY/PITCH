package org.cosmic.backend.domain.albumChat.dtos.comment;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.albumChat.domains.AlbumChatComment;
import org.cosmic.backend.domain.user.domains.User;
import org.cosmic.backend.domain.user.dtos.UserDetail;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlbumChatReplyDetail implements Serializable {

  private Long albumChatCommentId;
  private String content;
  private Instant createAt;
  private Instant updateAt;
  private UserDetail author;

  public AlbumChatReplyDetail(AlbumChatComment albumChatComment) {
    this.albumChatCommentId = albumChatComment.getAlbumChatCommentId();
    this.content = albumChatComment.getContent();
    this.createAt = albumChatComment.getCreateTime();
    this.updateAt = albumChatComment.getUpdateTime();
    this.author = User.toUserDetail(albumChatComment.getUser());
  }

  public static AlbumChatReplyDetail from(AlbumChatComment albumChatComment) {
    return AlbumChatReplyDetail.builder()
        .albumChatCommentId(albumChatComment.getAlbumChatCommentId())
        .content(albumChatComment.getContent())
        .createAt(albumChatComment.getCreateTime())
        .updateAt(albumChatComment.getUpdateTime())
        .author(UserDetail.from(albumChatComment.getUser()))
        .build();
  }

  public static List<AlbumChatReplyDetail> from(List<AlbumChatComment> children) {
    return children.stream()
        .map(AlbumChatReplyDetail::from)
        .sorted((o1, o2) -> o2.recentTime().compareTo(o1.recentTime()))
        .toList();
  }

  private Instant recentTime() {
    return updateAt == null ? createAt : updateAt;
  }
}