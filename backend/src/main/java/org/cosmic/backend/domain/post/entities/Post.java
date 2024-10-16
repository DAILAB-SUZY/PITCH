package org.cosmic.backend.domain.post.entities;

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
import org.cosmic.backend.domain.post.dtos.Post.PostAndCommentsDetail;
import org.cosmic.backend.domain.post.dtos.Post.PostDetail;
import org.cosmic.backend.domain.user.domains.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "post") // 테이블 이름 수정
public class Post {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "post_id") // 컬럼 이름 명시
  private Long postId;

  @Column(length = 3000)
  private String content; // 내용
  private Instant updateTime;
  @Builder.Default
  private Instant createTime = Instant.now();

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne
  @JoinColumn(name = "album_id")
  private Album album;

  @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
  @Builder.Default
  private List<PostComment> postComments = new ArrayList<>();

  @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private List<PostLike> postLikes = new ArrayList<>();

  public static PostDetail toPostDetail(Post post) {
    return PostDetail.builder()
        .postId(post.postId)
        .content(post.content)
        .createAt(post.createTime)
        .updateAt(post.updateTime)
        .album(Album.toAlbumDetail(post.album))
        .author(User.toUserDetail(post.user))
        .build();
  }

  public static PostAndCommentsDetail toPostAndCommentDetail(Post post) {
    return PostAndCommentsDetail.builder()
        .postDetail(PostDetail.builder()
            .postId(post.postId)
            .content(post.content)
            .createAt(post.createTime)
            .updateAt(post.updateTime)
            .album(Album.toAlbumDetail(post.album))
            .author(User.toUserDetail(post.user))
            .build())
        .comments(PostComment.toCommentDetails(post.postComments))
        .likes(post.postLikes.stream().map(like -> User.toUserDetail(like.getUser())).toList())
        .build();
  }

  public boolean isAuthorId(Long userId) {
    return getUser().isMe(userId);
  }

  public static List<PostAndCommentsDetail> toPostAndCommentDetail(List<Post> posts) {
    return posts.stream().map(Post::toPostAndCommentDetail).toList();
  }

}
