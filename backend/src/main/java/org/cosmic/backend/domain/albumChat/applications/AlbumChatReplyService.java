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
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
        if(commentRepository.findById(commentId).isEmpty()) {
            throw new NotFoundCommentException();
        }
        return replyRepository.findByAlbumChatComment_AlbumChatCommentId(commentId)
                .orElse(Collections.emptyList())
                .stream()
                .map(AlbumChatReplyResponse::new)
                .collect(Collectors.toList());
    }

    public AlbumChatReplyDto albumChatReplyCreate(AlbumChatReplyCreateReq reply) {
        if(userRepository.findById(reply.getUserId()).isEmpty()) {
            throw new NotFoundUserException();
        }
        if(commentRepository.findById(reply.getAlbumChatCommentId()).isEmpty()) {
            throw new NotFoundCommentException();
        }
        AlbumChatReply replyEntity=new AlbumChatReply(
                reply.getContent()
                ,Instant.now()
                ,commentRepository.findById(reply.getAlbumChatCommentId()).get()
                ,userRepository.findById(reply.getUserId()).get());
        return new AlbumChatReplyDto(replyRepository.save(replyEntity).getAlbumChatReplyId());
    }

    public void albumChatReplyUpdate(AlbumChatReplyUpdateReq reply) {
        if(replyRepository.findById(reply.getAlbumChatReplyId()).isEmpty()) {
            throw new NotFoundReplyException();
        }
        AlbumChatReply updatedReply=replyRepository.findByAlbumChatReplyId(reply.getAlbumChatReplyId());
        if(!updatedReply.getAlbumChatComment().getAlbumChatCommentId().equals(reply.getAlbumChatCommentId())) {
            throw new NotMatchCommentException();
        }
        if(!updatedReply.getUser().getUserId().equals(reply.getUserId())) {
            throw new NotMatchUserException();
        }
        updatedReply.setContent(reply.getContent());
        updatedReply.setUpdateTime(Instant.now());
        replyRepository.save(updatedReply);
    }

    public void albumChatReplyDelete(Long replyId) {
        if(replyRepository.findById(replyId).isEmpty()) {
            throw new NotFoundReplyException();
        }
        replyRepository.deleteById(replyId);
    }
}