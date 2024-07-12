package org.cosmic.backend.domain.albumChat.service;
import org.cosmic.backend.domain.albumChat.domain.AlbumChatReply;
import org.cosmic.backend.domain.albumChat.dto.reply.AlbumChatReplyDto;
import org.cosmic.backend.domain.albumChat.dto.reply.CreateAlbumChatReplyReq;
import org.cosmic.backend.domain.albumChat.dto.reply.UpdateAlbumChatReplyReq;
import org.cosmic.backend.domain.albumChat.repository.AlbumChatCommentRepository;
import org.cosmic.backend.domain.albumChat.repository.AlbumChatReplyRepository;
import org.cosmic.backend.domain.playList.exceptions.NotFoundUserException;
import org.cosmic.backend.domain.post.exception.NotFoundCommentException;
import org.cosmic.backend.domain.post.exception.NotFoundReplyException;
import org.cosmic.backend.domain.user.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class AlbumChatReplyService {
    @Autowired
    private AlbumChatReplyRepository replyRepository;
    @Autowired
    private UsersRepository userRepository;
    @Autowired
    private AlbumChatCommentRepository commentRepository;

    public List<UpdateAlbumChatReplyReq> getAlbumChatRepliesByCommentId(Long commentId) {//해당 댓글에 있는 모든 reply가져오기
        List<UpdateAlbumChatReplyReq> replies = new ArrayList<>();
        if(!commentRepository.findById(commentId).isPresent()) {
            throw new NotFoundCommentException();
        }
        else{
            List<AlbumChatReply> replyList=replyRepository.findByAlbumChatComment_AlbumChatCommentId(commentId).get();
            for (AlbumChatReply reply : replyList) {
                UpdateAlbumChatReplyReq replyreq = new UpdateAlbumChatReplyReq();
                replyreq.setAlbumChatReplyId(reply.getAlbumChatReplyId());
                replyreq.setContent(reply.getContent());
                replyreq.setCreateTime(reply.getUpdateTime());
                replyreq.setUserId(reply.getUser().getUserId());
                replyreq.setAlbumChatCommentId(reply.getAlbumChatComment().getAlbumChatCommentId());
                replies.add(replyreq);
            }
            return replies;
        }
    }

    public AlbumChatReplyDto createAlbumChatReply(CreateAlbumChatReplyReq reply) {
        //누가 쓴 comment인지 나와야하기 때문에 userId필요
        //없는 comment인지 확인필요
        AlbumChatReply replyEntity=new AlbumChatReply();
        if(!userRepository.findById(reply.getUserId()).isPresent()) {
            throw new NotFoundUserException();
        }
        else if(!commentRepository.findById(reply.getAlbumChatCommentId()).isPresent())
        {
            throw new NotFoundCommentException();
        }
        replyEntity.setContent(reply.getContent());
        replyEntity.setUpdateTime(Instant.now());
        replyEntity.setUser(userRepository.findByUserId(reply.getUserId()).get());
        replyEntity.setAlbumChatComment(commentRepository.findByAlbumChatCommentId(reply.getAlbumChatCommentId()));//어떤 댓글의 대댓글인지 알아야함.
        replyRepository.save(replyEntity);
        AlbumChatReplyDto replyDto=new AlbumChatReplyDto();
        replyDto.setAlbumChatReplyId(replyEntity.getAlbumChatReplyId());
        return replyDto;
    }

    public void updateAlbumChatReply(UpdateAlbumChatReplyReq reply) {
        if(!replyRepository.findById(reply.getAlbumChatReplyId()).isPresent()) {
            throw new NotFoundReplyException();
        }
        else{
            AlbumChatReply reply1=replyRepository.findByAlbumChatReplyId(reply.getAlbumChatReplyId());
            reply1.setContent(reply.getContent());
            reply1.setUpdateTime(Instant.now());
            replyRepository.save(reply1);//새로생기는지 업데이트만 되는지 만약 새로생기는거면업데이트만 되게만들어야함.
        }
    }

    public void deleteAlbumChatReply(Long replyId) {
        if(!replyRepository.findById(replyId).isPresent()) {
            throw new NotFoundReplyException();
        }
        replyRepository.deleteById(replyId);
    }
}