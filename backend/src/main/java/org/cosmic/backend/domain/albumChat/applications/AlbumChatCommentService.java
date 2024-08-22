package org.cosmic.backend.domain.albumChat.applications;

import org.cosmic.backend.domain.albumChat.domains.AlbumChatComment;
import org.cosmic.backend.domain.albumChat.dtos.comment.AlbumChatCommentCreateReq;
import org.cosmic.backend.domain.albumChat.dtos.comment.AlbumChatCommentDto;
import org.cosmic.backend.domain.albumChat.dtos.comment.AlbumChatCommentResponse;
import org.cosmic.backend.domain.albumChat.dtos.comment.AlbumChatCommentUpdateReq;
import org.cosmic.backend.domain.albumChat.exceptions.NotFoundAlbumChatCommentException;
import org.cosmic.backend.domain.albumChat.exceptions.NotFoundAlbumChatException;
import org.cosmic.backend.domain.albumChat.exceptions.NotMatchAlbumChatException;
import org.cosmic.backend.domain.albumChat.repositorys.AlbumChatCommentRepository;
import org.cosmic.backend.domain.albumChat.repositorys.AlbumChatRepository;
import org.cosmic.backend.domain.playList.exceptions.NotFoundUserException;
import org.cosmic.backend.domain.user.repositorys.UsersRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AlbumChatCommentService {
    private final AlbumChatRepository albumChatRepository;
    private final AlbumChatCommentRepository commentRepository;
    private final UsersRepository userRepository;

    public AlbumChatCommentService(AlbumChatRepository albumChatRepository, AlbumChatCommentRepository commentRepository, UsersRepository userRepository) {
        this.albumChatRepository = albumChatRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }

    public List<AlbumChatCommentResponse> getCommentsByAlbumChatId(Long albumChatId) {
        if(albumChatRepository.findById(albumChatId).isEmpty()) {
            throw new NotFoundAlbumChatException();
        }
        return commentRepository.findByAlbumChat_AlbumChatId(albumChatId)
                .orElse(Collections.emptyList())
                .stream()
                .map(AlbumChatCommentResponse::new)
                .collect(Collectors.toList());
    }

    public AlbumChatCommentDto albumChatCommentCreate(AlbumChatCommentCreateReq comment) {
        if(albumChatRepository.findById(comment.getAlbumChatId()).isEmpty()) {
            throw new NotFoundAlbumChatException();
        }
        if(userRepository.findById(comment.getUserId()).isEmpty()) {
            throw new NotFoundUserException();
        }
        AlbumChatComment commentEntity = commentRepository.save(
                new AlbumChatComment(
                    comment.getContent()
                    ,Instant.now()
                    ,userRepository.findById(comment.getUserId()).get()
                    ,null
                    ,albumChatRepository.findById(comment.getAlbumChatId()).get()));
        return new AlbumChatCommentDto(commentEntity);
    }

    public void albumChatCommentUpdate(AlbumChatCommentUpdateReq comment) {
        if(commentRepository.findById(comment.getAlbumChatCommentId()).isEmpty()) {
            throw new NotFoundAlbumChatCommentException();
        }
        if(userRepository.findById(comment.getUserId()).isEmpty()) {
            throw new NotFoundUserException();
        }
        AlbumChatComment updatedComment = commentRepository.findById(comment.getAlbumChatCommentId()).get();
        if(!updatedComment.getAlbumChat().getAlbumChatId().equals(comment.getAlbumChatId())) {
            throw new NotMatchAlbumChatException();
        }
        updatedComment.setContent(comment.getContent());
        commentRepository.save(updatedComment);
    }

    public void albumChatCommentDelete(AlbumChatCommentDto commentDto) {
        if(commentRepository.findById(commentDto.getAlbumChatCommentId()).isEmpty()) {
            throw new NotFoundAlbumChatCommentException();
        }
        commentRepository.deleteById(commentDto.getAlbumChatCommentId());
    }
}