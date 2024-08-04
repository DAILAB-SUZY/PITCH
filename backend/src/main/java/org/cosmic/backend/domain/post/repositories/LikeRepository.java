package org.cosmic.backend.domain.post.repositories;

import org.cosmic.backend.domain.post.entities.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface LikeRepository extends JpaRepository<Like,Long> {
    Like findByLikeId(Long likeId);//key로 찾기
    List<Like> findByPost_PostId(Long postId);
    Like findByUser_UserId(Long userId);
    Optional<Like> findByPost_PostIdAndUser_UserId(Long postId, Long userId);
}
