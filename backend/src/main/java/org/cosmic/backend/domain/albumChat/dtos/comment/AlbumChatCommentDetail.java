package org.cosmic.backend.domain.albumChat.dtos.comment;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    this.author = User.toUserDetail(albumChatComment.getUser());
  }

  private static Map<AlbumChatComment, List<AlbumChatComment>> getGroupedMap(
      List<AlbumChatComment> postComments) {
    Map<AlbumChatComment, List<AlbumChatComment>> groupedMap = new HashMap<>();
    for (AlbumChatComment postComment : postComments) {
      try {
        groupedMap.get(postComment.getParentAlbumChatComment()).add(postComment);
      } catch (NullPointerException e) {
        groupedMap.put(postComment, new ArrayList<>());
      }
    }
    return groupedMap;
  }

  public static List<AlbumChatCommentDetail> from(List<AlbumChatComment> albumChatComments) {
    return getGroupedMap(albumChatComments).entrySet()
        .stream()
        .map(entry -> AlbumChatCommentDetail.from(entry.getKey(), entry.getValue()))
        .sorted((o1, o2) -> o2.recentTime().compareTo(o1.recentTime()))
        .toList();
  }

  private static AlbumChatCommentDetail from(AlbumChatComment parent,
      List<AlbumChatComment> children) {
    AlbumChatCommentDetail detail = AlbumChatCommentDetail.from(parent);
    detail.setComments(AlbumChatReplyDetail.from(children));
    return detail;
  }

  private static AlbumChatCommentDetail from(AlbumChatComment albumChatComment) {
    return AlbumChatCommentDetail.builder()
        .albumChatCommentId(albumChatComment.getAlbumChatCommentId())
        .content(albumChatComment.getContent())
        .createAt(albumChatComment.getCreateTime())
        .updateAt(albumChatComment.getUpdateTime())
        .likes(UserDetail.from(
            albumChatComment.getAlbumChatCommentLikes().stream().map(AlbumChatCommentLike::getUser)
                .toList()))
        .comments(List.of())
        .author(UserDetail.from(albumChatComment.getUser()))
        .build();
  }

  private Instant recentTime() {
    return updateAt == null ? createAt : updateAt;
  }
}
