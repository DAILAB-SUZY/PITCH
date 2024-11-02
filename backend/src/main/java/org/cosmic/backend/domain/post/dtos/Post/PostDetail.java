package org.cosmic.backend.domain.post.dtos.Post;

import java.time.Instant;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.post.entities.Post;
import org.cosmic.backend.domain.user.dtos.UserDetail;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDetail {

  private Long postId;
  private String content;
  private Instant createAt;
  private Instant updateAt;
  private UserDetail author;
  private AlbumDetail album;

  public static PostDetail from(Post post) {
    return PostDetail.builder()
        .postId(post.getPostId())
        .content(post.getContent())
        .createAt(post.getCreateTime())
        .updateAt(post.getUpdateTime())
        .author(UserDetail.from(post.getUser()))
        .album(AlbumDetail.from(post.getAuthorAlbumScore()))
        .build();
  }

  public static List<PostDetail> from(List<Post> posts) {
    return posts.stream().map(PostDetail::from).toList();
  }
}
