package org.cosmic.backend.domain.post.dtos.Comment;

import java.time.Instant;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.post.entities.PostComment;
import org.cosmic.backend.domain.user.dtos.UserDetail;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChildCommentDetail {

  private Long id;
  private String content;
  private UserDetail author;
  private Instant createTime;
  private Instant updateTime;

  public static ChildCommentDetail from(PostComment comment) {
    return ChildCommentDetail.builder()
        .id(comment.getCommentId())
        .content(comment.getContent())
        .author(UserDetail.from(comment.getUser()))
        .createTime(comment.getCreateTime())
        .updateTime(comment.getUpdateTime())
        .build();
  }

  public static List<ChildCommentDetail> from(List<PostComment> children) {
    return children.stream()
        .map(ChildCommentDetail::from)
        .sorted((o1, o2) -> o2.recentTime()
            .compareTo(o1.recentTime()))
        .toList();
  }

  private Instant recentTime() {
    return updateTime == null ? createTime : updateTime;
  }
}
