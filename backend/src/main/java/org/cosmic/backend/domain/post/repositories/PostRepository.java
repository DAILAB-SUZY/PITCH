package org.cosmic.backend.domain.post.repositories;

import java.util.List;
import java.util.Optional;
import org.cosmic.backend.domain.post.entities.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostRepository extends JpaRepository<Post, Long> {

  List<Post> findByUser_UserId(Long userId);//key로 찾기

  Post findByPostId(Long postId);

  Optional<Post> findByContentLike(String content);

  Optional<Post> findByContent(String content);

  Page<Post> findAll(Pageable pageable);

  @Query("SELECT p FROM Post p ORDER BY COALESCE(p.updateTime, p.createTime) DESC")
  Page<Post> findAllWithCustomSorting(Pageable pageable);

  Page<Post> findByUser_UserId(Long userId, Pageable pageable);
}
