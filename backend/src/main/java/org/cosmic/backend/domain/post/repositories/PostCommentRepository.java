package org.cosmic.backend.domain.post.repositories;
import org.cosmic.backend.domain.post.entities.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostCommentRepository extends JpaRepository<PostComment,Long> {
    List<PostComment> findByPost_PostId(Long postid);//key로 찾기
    PostComment findByCommentId(Long commentid);
}
