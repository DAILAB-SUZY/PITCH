package org.cosmic.backend.domain.post.service;

import org.cosmic.backend.domain.playList.exceptions.NotFoundUserException;
import org.cosmic.backend.domain.post.dto.Reply.CreateReplyReq;
import org.cosmic.backend.domain.post.dto.Reply.ReplyDto;
import org.cosmic.backend.domain.post.dto.Reply.UpdateReplyReq;
import org.cosmic.backend.domain.post.entity.Reply;
import org.cosmic.backend.domain.post.exception.NotFoundCommentException;
import org.cosmic.backend.domain.post.exception.NotFoundReplyException;
import org.cosmic.backend.domain.post.exception.NotMatchCommentException;
import org.cosmic.backend.domain.post.exception.NotMatchUserException;
import org.cosmic.backend.domain.post.repository.CommentRepository;
import org.cosmic.backend.domain.post.repository.ReplyRepository;
import org.cosmic.backend.domain.user.repositorys.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReplyService {
    @Autowired
    private ReplyRepository replyRepository;
    @Autowired
    private UsersRepository userRepository;
    @Autowired
    private CommentRepository commentRepository;

    public List<UpdateReplyReq> getRepliesByCommentId(Long commentId) {//해당 댓글에 있는 모든 reply가져오기
        List<UpdateReplyReq> replies = new ArrayList<>();
        if(!commentRepository.findById(commentId).isPresent()) {
            throw new NotFoundCommentException();
        }
        else{
            List<Reply> replyList=replyRepository.findByComment_CommentId(commentId);
            for (Reply reply : replyList) {
                UpdateReplyReq replyreq = new UpdateReplyReq();
                //여기서 앨범을 찾는 과정 필요}
                replyreq.setReplyId(reply.getReplyId());
                replyreq.setContent(reply.getContent());
                replyreq.setCreateTime(reply.getUpdateTime());
                replyreq.setUserId(reply.getUser().getUserId());
                replyreq.setCommentId(reply.getComment().getCommentId());
                replies.add(replyreq);
            }
            return replies;
        }
    }

    public ReplyDto createReply(CreateReplyReq reply) {
        //누가 쓴 comment인지 나와야하기 때문에 userId필요
        //없는 comment인지 확인필요
        Reply replyEntity=new Reply();
        if(!userRepository.findById(reply.getUserId()).isPresent()) {
            throw new NotFoundUserException();
        }
        else if(!commentRepository.findById(reply.getCommentId()).isPresent())
        {
            throw new NotFoundCommentException();
        }
        replyEntity.setContent(reply.getContent());
        replyEntity.setUpdateTime(Instant.now());
        replyEntity.setUser(userRepository.findByUserId(reply.getUserId()).get());
        replyEntity.setComment(commentRepository.findByCommentId(reply.getCommentId()));//어떤 댓글의 대댓글인지 알아야함.
        replyRepository.save(replyEntity);
        ReplyDto replyDto=new ReplyDto();
        replyDto.setReplyId(replyEntity.getReplyId());
        return replyDto;
    }

    public void updateReply(UpdateReplyReq reply) {
        if(!replyRepository.findById(reply.getReplyId()).isPresent()) {
            throw new NotFoundReplyException();
        }
        else{
            Reply reply1=replyRepository.findByReplyId(reply.getReplyId());
            if(reply1.getComment().getCommentId()!=reply.getCommentId())
            {
                throw new NotMatchCommentException();
            }
            else if(reply1.getUser().getUserId()!=reply.getUserId())
            {
                throw new NotMatchUserException();
            }
            reply1.setContent(reply.getContent());
            replyRepository.save(reply1);//새로생기는지 업데이트만 되는지 만약 새로생기는거면업데이트만 되게만들어야함.
        }
    }

    public void deleteReply(Long replyId) {
        if(!replyRepository.findById(replyId).isPresent()) {
            throw new NotFoundReplyException();
        }
        replyRepository.deleteById(replyId);
    }
}