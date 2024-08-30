package org.cosmic.backend.domain.post.repositories;

import org.cosmic.backend.domain.post.entities.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ReplyRepository extends JpaRepository<Reply,Long> {
    List<Reply> findByComment_CommentId(Long commentId);//key로 찾기
    Reply findByReplyId(Long replyId);
}
