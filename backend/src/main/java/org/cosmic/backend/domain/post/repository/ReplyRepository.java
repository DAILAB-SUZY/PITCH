package org.cosmic.backend.domain.post.repository;

import org.cosmic.backend.domain.post.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ReplyRepository extends JpaRepository<Reply,Long> {
    List<Reply> findByComment_CommentId(Long commentId);//key로 찾기
    Reply findByReplyId(Long replyId);
}
