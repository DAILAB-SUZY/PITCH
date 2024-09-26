package org.cosmic.backend.domain.post.repositories;

import org.cosmic.backend.domain.post.entities.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post,Long> {
    List<Post> findByUser_UserId(Long userId);//key로 찾기
    Post findByPostId(Long postId);

    Optional<Post> findByContentLike(String content);

    Optional<Post> findByContent(String content);

    Page<Post> findAll(Pageable pageable);
}
