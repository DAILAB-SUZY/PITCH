package org.cosmic.backend.domain.post.repositories;

import java.util.Optional;
import org.cosmic.backend.domain.post.entities.PostCommentLike;
import org.cosmic.backend.domain.post.entities.PostCommentLikePK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostCommentLikeRepository extends
    JpaRepository<PostCommentLike, PostCommentLikePK> {

  Long countByPostComment_CommentId(Long postCommentId);

  void deleteByPostComment_CommentIdAndUser_UserId(Long commentId, Long userId);

  Optional<PostCommentLike> findByPostComment_CommentIdAndUser_UserId(Long commentId, Long userId);
}
