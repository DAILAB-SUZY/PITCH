package org.cosmic.backend.domain.post.applications;

import org.cosmic.backend.domain.playList.exceptions.NotFoundUserException;
import org.cosmic.backend.domain.post.dtos.Reply.CreateReplyReq;
import org.cosmic.backend.domain.post.dtos.Reply.ReplyDto;
import org.cosmic.backend.domain.post.dtos.Reply.UpdateReplyReq;
import org.cosmic.backend.domain.post.entities.Reply;
import org.cosmic.backend.domain.post.exceptions.NotFoundCommentException;
import org.cosmic.backend.domain.post.exceptions.NotFoundReplyException;
import org.cosmic.backend.domain.post.exceptions.NotMatchCommentException;
import org.cosmic.backend.domain.post.exceptions.NotMatchUserException;
import org.cosmic.backend.domain.post.repositories.CommentRepository;
import org.cosmic.backend.domain.post.repositories.ReplyRepository;
import org.cosmic.backend.domain.user.repositorys.UsersRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

@Service
public class ReplyService {
    private final ReplyRepository replyRepository;
    private final UsersRepository userRepository;
    private final CommentRepository commentRepository;

    public ReplyService(ReplyRepository replyRepository, UsersRepository userRepository, CommentRepository commentRepository) {
        this.replyRepository = replyRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
    }

    public List<UpdateReplyReq> getRepliesByCommentId(Long commentId) {//해당 댓글에 있는 모든 reply가져오기
        if(commentRepository.findById(commentId).isEmpty()) {
            throw new NotFoundCommentException();
        }
        return replyRepository.findByComment_CommentId(commentId)
                .stream()
                .map(Reply::toUpdateReplyReq)
                .toList();
    }

    public ReplyDto createReply(CreateReplyReq reply) {
        //누가 쓴 comment인지 나와야하기 때문에 userId필요
        //없는 comment인지 확인필요
        if(userRepository.findById(reply.getUserId()).isEmpty()) {
            throw new NotFoundUserException();
        }
        else if(commentRepository.findById(reply.getCommentId()).isEmpty()) {
            throw new NotFoundCommentException();
        }
        return Reply.toReplyDto(replyRepository.save(Reply.builder()
                .content(reply.getContent())
                .updateTime(Instant.now())
                .user(userRepository.findByUserId(reply.getUserId()).orElseThrow())
                .comment(commentRepository.findByCommentId(reply.getCommentId()))//어떤 댓의 대댓글인지 알아야함.
                .build()));
    }

    public void updateReply(UpdateReplyReq replyReq) {
        if(replyRepository.findById(replyReq.getReplyId()).isEmpty()) {
            throw new NotFoundReplyException();
        }
        Reply updatedReply=replyRepository.findByReplyId(replyReq.getReplyId());
        if(!Objects.equals(updatedReply.getComment().getCommentId(), replyReq.getCommentId())) {
            throw new NotMatchCommentException();
        }
        else if(!Objects.equals(updatedReply.getUser().getUserId(), replyReq.getUserId())) {
            throw new NotMatchUserException();
        }
        updatedReply.setContent(replyReq.getContent());
        replyRepository.save(updatedReply);//새로생기는지 업데이트만 되는지 만약 새로생기는거면업데이트만 되게만들어야함.else{
    }

    public void deleteReply(Long replyId) {
        if(replyRepository.findById(replyId).isEmpty()) {
            throw new NotFoundReplyException();
        }
        replyRepository.deleteById(replyId);
    }
}