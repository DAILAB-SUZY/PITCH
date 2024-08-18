package org.cosmic.backend.domain.albumChat.applications;

import org.cosmic.backend.domain.albumChat.domains.AlbumChatReply;
import org.cosmic.backend.domain.albumChat.dtos.reply.AlbumChatReplyCreateReq;
import org.cosmic.backend.domain.albumChat.dtos.reply.AlbumChatReplyDto;
import org.cosmic.backend.domain.albumChat.dtos.reply.AlbumChatReplyResponse;
import org.cosmic.backend.domain.albumChat.dtos.reply.AlbumChatReplyUpdateReq;
import org.cosmic.backend.domain.albumChat.repositorys.AlbumChatCommentRepository;
import org.cosmic.backend.domain.albumChat.repositorys.AlbumChatReplyRepository;
import org.cosmic.backend.domain.playList.exceptions.NotFoundUserException;
import org.cosmic.backend.domain.post.exceptions.NotFoundCommentException;
import org.cosmic.backend.domain.post.exceptions.NotFoundReplyException;
import org.cosmic.backend.domain.post.exceptions.NotMatchCommentException;
import org.cosmic.backend.domain.post.exceptions.NotMatchUserException;
import org.cosmic.backend.domain.user.repositorys.UsersRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class AlbumChatReplyService {
    private final AlbumChatReplyRepository replyRepository;
    private final UsersRepository userRepository;
    private final AlbumChatCommentRepository commentRepository;

    public AlbumChatReplyService(AlbumChatReplyRepository replyRepository, UsersRepository userRepository, AlbumChatCommentRepository commentRepository) {
        this.replyRepository = replyRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
    }

    public List<AlbumChatReplyResponse> albumChatRepliesGetByCommentId(Long commentId) {
        List<AlbumChatReplyResponse> replies = new ArrayList<>();
        if(commentRepository.findById(commentId).isEmpty()) {
            throw new NotFoundCommentException();
        }
        List<AlbumChatReply> replyList=replyRepository.findByAlbumChatComment_AlbumChatCommentId(commentId).get();
        for (AlbumChatReply reply : replyList) {
            AlbumChatReplyResponse replyreq = new AlbumChatReplyResponse(
                reply.getAlbumChatReplyId(),reply.getUser().getUserId(),reply.getContent(),reply.getUpdateTime()
            );
            replies.add(replyreq);
        }
        return replies;
    }

    public AlbumChatReplyDto albumChatReplyCreate(AlbumChatReplyCreateReq reply) {
        if(userRepository.findById(reply.getUserId()).isEmpty()) {
            throw new NotFoundUserException();
        }
        if(commentRepository.findById(reply.getAlbumChatCommentId()).isEmpty())
        {
            throw new NotFoundCommentException();
        }
        AlbumChatReply replyEntity=new AlbumChatReply(reply.getContent(),Instant.now()
            ,commentRepository.findByAlbumChatCommentId(reply.getAlbumChatCommentId())
                ,userRepository.findById(reply.getUserId()).get());
        replyRepository.save(replyEntity);
        AlbumChatReplyDto replyDto=new AlbumChatReplyDto(replyEntity.getAlbumChatReplyId());
        return replyDto;
    }

    public void albumChatReplyUpdate(AlbumChatReplyUpdateReq reply) {
        if(replyRepository.findById(reply.getAlbumChatReplyId()).isEmpty()) {
            throw new NotFoundReplyException();
        }
        AlbumChatReply reply1=replyRepository.findByAlbumChatReplyId(reply.getAlbumChatReplyId());
        if(!reply1.getAlbumChatComment().getAlbumChatCommentId().equals(reply.getAlbumChatCommentId())) {
            throw new NotMatchCommentException();
        }
        if(!reply1.getUser().getUserId().equals(reply.getUserId()))
        {
            throw new NotMatchUserException();
        }
        reply1.setContent(reply.getContent());
        reply1.setUpdateTime(Instant.now());
        replyRepository.save(reply1);
    }

    public void albumChatReplyDelete(Long replyId) {
        if(replyRepository.findById(replyId).isEmpty()) {
            throw new NotFoundReplyException();
        }
        replyRepository.deleteById(replyId);
    }
}