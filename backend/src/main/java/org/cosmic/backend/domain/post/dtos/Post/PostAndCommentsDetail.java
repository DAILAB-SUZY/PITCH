package org.cosmic.backend.domain.post.dtos.Post;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.post.dtos.Comment.CommentDetail;
import org.cosmic.backend.domain.post.entities.Post;
import org.cosmic.backend.domain.post.entities.PostLike;
import org.cosmic.backend.domain.user.dtos.UserDetail;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostAndCommentsDetail {

  private PostDetail postDetail;
  private List<CommentDetail> comments;
  private List<UserDetail> likes;

  public static PostAndCommentsDetail from(Post post) {
    return PostAndCommentsDetail.builder()
        .postDetail(PostDetail.from(post))
        .comments(CommentDetail.from(post.getPostComments()))
        .likes(UserDetail.from(post.getPostLikes().stream().map(PostLike::getUser).toList()))
        .build();
  }
}
