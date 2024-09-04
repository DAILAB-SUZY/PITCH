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
import org.cosmic.backend.domain.post.exceptions.NotMatchUserException;
import org.cosmic.backend.domain.user.repositorys.UsersRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * AlbumChatCommentService 클래스는 앨범 챗의 댓글 관리 기능을 제공합니다.
 * 댓글 조회, 생성, 수정, 삭제와 관련된 비즈니스 로직을 처리합니다.
 */
@Service
public class AlbumChatCommentService {
    private final AlbumChatRepository albumChatRepository;
    private final AlbumChatCommentRepository commentRepository;
    private final UsersRepository userRepository;

    /**
     * AlbumChatCommentService 생성자.
     *
     * @param albumChatRepository 앨범 챗 저장소 주입
     * @param commentRepository 댓글 저장소 주입
     * @param userRepository 사용자 저장소 주입
     */
    public AlbumChatCommentService
    (AlbumChatRepository albumChatRepository, AlbumChatCommentRepository commentRepository,
     UsersRepository userRepository) {
        this.albumChatRepository = albumChatRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }

    /**
     * 특정 앨범 챗 ID로 댓글 목록을 조회합니다.
     *
     * @param albumChatId 조회할 앨범 챗 ID
     * @return List<AlbumChatCommentResponse> 조회된 댓글 목록
     * @throws NotFoundAlbumChatException 앨범 챗이 존재하지 않을 경우 발생
     */
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

    /**
     * 새로운 앨범 챗 댓글을 생성합니다.
     *
     * @param comment 생성할 댓글 정보가 담긴 AlbumChatCommentCreateReq 객체
     * @return AlbumChatCommentDto 생성된 댓글 정보
     * @throws NotFoundAlbumChatException 앨범 챗이 존재하지 않을 경우 발생
     * @throws NotFoundUserException 사용자가 존재하지 않을 경우 발생
     */
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

    /**
     * 기존 앨범 챗 댓글을 수정합니다.
     *
     * @param comment 수정할 댓글 정보가 담긴 AlbumChatCommentUpdateReq 객체
     * @throws NotFoundAlbumChatCommentException 댓글이 존재하지 않을 경우 발생
     * @throws NotFoundUserException 사용자가 존재하지 않을 경우 발생
     * @throws NotMatchAlbumChatException 댓글이 속한 앨범 챗이 일치하지 않을 경우 발생
     * @throws NotMatchUserException 수정하려는 사용자와 기존 댓글 생성한 사용자가 다를때 발생
     */
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
        if(!updatedComment.getUser().getUserId().equals(comment.getUserId())) {
            throw new NotMatchUserException();
        }
        updatedComment.setContent(comment.getContent());
        commentRepository.save(updatedComment);
        //사용자가 다를때 1!=2인데 바꾸려고하는경우
    }
    /**
     * 앨범 챗 댓글을 삭제합니다. 댓글 삭제하면 해당 댓글에 붙어있던 좋아요, 대댓글 등 모두 삭제됩니다.
     *
     * @param commentDto 삭제할 댓글 정보가 담긴 AlbumChatCommentDto객체
     * @throws NotFoundAlbumChatCommentException 댓글이 존재하지 않을 경우 발생
     */
    public void albumChatCommentDelete(AlbumChatCommentDto commentDto) {
        if(commentRepository.findById(commentDto.getAlbumChatCommentId()).isEmpty()) {
            throw new NotFoundAlbumChatCommentException();
        }
        //TODO 삭제하려는 사용자와 해당 글 올린 사용자와 다를때
        commentRepository.deleteById(commentDto.getAlbumChatCommentId());
    }
}