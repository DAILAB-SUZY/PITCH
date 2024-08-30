package org.cosmic.backend.domain.post.repositories;

import org.cosmic.backend.domain.post.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post,Long> {
    List<Post> findByUser_UserId(Long userId);//key로 찾기
    Post findByPostId(Long postId);
}
