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
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.user.domains.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "post_comment") // 테이블 이름 수정
@Builder
@EqualsAndHashCode(of = {"commentId", "parentComment"})
public class PostComment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "comment_id") // 컬럼 이름 명시
  private Long commentId;

  @ManyToOne(fetch = FetchType.LAZY)
  private PostComment parentComment;

  @ManyToOne
  @JoinColumn(name = "post_id")
  private Post post;

  private String content;

  @Column(name = "create_time")
  @Builder.Default
  private Instant createTime = Instant.now();

  @Column(name = "update_time")
  private Instant updateTime;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @OneToMany(mappedBy = "postComment", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private List<PostCommentLike> postCommentLikes = new ArrayList<>();
}
