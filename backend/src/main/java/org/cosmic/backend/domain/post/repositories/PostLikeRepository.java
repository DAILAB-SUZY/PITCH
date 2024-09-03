package org.cosmic.backend.domain.post.repositories;

import org.cosmic.backend.domain.post.entities.PostLike;
import org.cosmic.backend.domain.post.entities.PostLikePK;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface PostLikeRepository extends JpaRepository<PostLike, PostLikePK> {
    List<PostLike> findByPost_PostId(Long postId);
    PostLike findByUser_UserId(Long userId);
    Optional<PostLike> findByPost_PostIdAndUser_UserId(Long postId, Long userId);
}
