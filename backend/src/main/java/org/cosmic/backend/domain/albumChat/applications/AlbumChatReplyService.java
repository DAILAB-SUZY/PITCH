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

/**
 * AlbumChatReplyService 클래스는 앨범 챗 댓글에 대한 답글 기능을 처리하는 서비스 로직을 제공합니다.
 */
@Service
public class AlbumChatReplyService {
    private final AlbumChatReplyRepository replyRepository;
    private final UsersRepository userRepository;
    private final AlbumChatCommentRepository commentRepository;

    /**
     * AlbumChatReplyService 생성자.
     *
     * @param replyRepository 앨범 챗 댓글 답글 리포지토리 객체
     * @param userRepository 사용자 리포지토리 객체
     * @param commentRepository 앨범 챗 댓글 리포지토리 객체
     */
    public AlbumChatReplyService(AlbumChatReplyRepository replyRepository, UsersRepository userRepository, AlbumChatCommentRepository commentRepository) {
        this.replyRepository = replyRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
    }

    /**
     * 특정 댓글에 대한 답글 목록을 조회합니다.
     *
     * @param commentId 조회할 댓글 ID
     * @return List<AlbumChatReplyResponse> 답글 목록 반환
     * @throws NotFoundCommentException 댓글이 존재하지 않을 경우 발생
     */
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

    /**
     * 새로운 답글을 생성합니다.
     *
     * @param reply 답글 생성 요청 정보(사용자ID와 댓글 ID 및 답글 내용)
     * @return AlbumChatReplyDto 생성된 답글 ID를 포함한 DTO 객체
     * @throws NotFoundUserException 사용자가 존재하지 않을 경우 발생
     * @throws NotFoundCommentException 댓글이 존재하지 않을 경우 발생
     */
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

    /**
     * 기존 답글을 업데이트합니다.
     *
     * @param reply 업데이트할 답글 정보
     * @throws NotFoundReplyException 답글이 존재하지 않을 경우 발생
     * @throws NotMatchCommentException 답글이 해당 댓글에 속하지 않는 경우 발생
     * @throws NotMatchUserException 사용자가 일치하지 않는 경우 발생
     */
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
        //TODO 사용자와 만든 사용자 정보 맞지않을 때
        updatedReply.setContent(reply.getContent());
        updatedReply.setUpdateTime(Instant.now());
        replyRepository.save(updatedReply);
    }

    /**
     * 특정 답글을 삭제합니다.
     *
     * @param replyId 삭제할 답글 ID
     * @throws NotFoundReplyException 답글이 존재하지 않을 경우 발생
     */
    public void albumChatReplyDelete(Long replyId) {
        if(replyRepository.findById(replyId).isEmpty()) {
            throw new NotFoundReplyException();
        }
        replyRepository.deleteById(replyId);
    }
}