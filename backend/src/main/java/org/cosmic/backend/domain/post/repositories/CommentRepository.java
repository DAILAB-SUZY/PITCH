package org.cosmic.backend.domain.post.repositories;
import org.cosmic.backend.domain.post.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    List<Comment> findByPost_PostId(Long postid);//key로 찾기
    Comment findByCommentId(Long commentid);
}
