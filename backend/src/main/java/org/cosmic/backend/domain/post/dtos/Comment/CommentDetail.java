package org.cosmic.backend.domain.post.dtos.Comment;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.post.entities.PostComment;
import org.cosmic.backend.domain.post.entities.PostCommentLike;
import org.cosmic.backend.domain.user.dtos.UserDetail;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDetail {

  private Long id;
  private String content;
  private Instant createdAt;
  private Instant updatedAt;
  private List<UserDetail> likes;
  private List<ChildCommentDetail> childComments;
  private UserDetail author;

  private static Map<PostComment, List<PostComment>> getGroupedMap(List<PostComment> postComments) {
    Map<PostComment, List<PostComment>> groupedMap = new HashMap<>();
    for (PostComment postComment : postComments) {
      try {
        groupedMap.get(postComment.getParentComment()).add(postComment);
      } catch (NullPointerException e) {
        groupedMap.put(postComment, new ArrayList<>());
      }
    }
    return groupedMap;
  }

  public static CommentDetail from(PostComment postComment) {
    return CommentDetail.builder()
        .id(postComment.getCommentId())
        .content(postComment.getContent())
        .createdAt(postComment.getCreateTime())
        .updatedAt(postComment.getUpdateTime())
        .likes(UserDetail.from(
            postComment.getPostCommentLikes().stream().map(PostCommentLike::getUser).toList()))
        .childComments(List.of())
        .author(UserDetail.from(postComment.getUser()))
        .build();
  }

  public static CommentDetail from(PostComment parent, List<PostComment> children) {
    CommentDetail commentDetail = CommentDetail.from(parent);
    commentDetail.setChildComments(ChildCommentDetail.from(children));
    return commentDetail;
  }

  public static List<CommentDetail> from(List<PostComment> postComments) {
    return getGroupedMap(postComments).entrySet()
        .stream()
        .map(entry -> CommentDetail.from(entry.getKey(), entry.getValue()))
        .sorted((o1, o2) -> o2.recentTime().compareTo(o1.recentTime()))
        .toList();
  }

  private Instant recentTime() {
    return updatedAt == null ? createdAt : updatedAt;
  }
}
