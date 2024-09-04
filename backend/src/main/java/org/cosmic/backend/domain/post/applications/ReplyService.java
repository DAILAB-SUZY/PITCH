package org.cosmic.backend.domain.post.applications;

import org.cosmic.backend.domain.playList.exceptions.NotFoundUserException;
import org.cosmic.backend.domain.post.dtos.Reply.CreateReplyReq;
import org.cosmic.backend.domain.post.dtos.Reply.ReplyDto;
import org.cosmic.backend.domain.post.dtos.Reply.UpdateReplyReq;
import org.cosmic.backend.domain.post.entities.PostComment;
import org.cosmic.backend.domain.post.exceptions.NotFoundCommentException;
import org.cosmic.backend.domain.post.exceptions.NotFoundReplyException;
import org.cosmic.backend.domain.post.exceptions.NotMatchCommentException;
import org.cosmic.backend.domain.post.exceptions.NotMatchUserException;
import org.cosmic.backend.domain.post.repositories.PostCommentRepository;
import org.cosmic.backend.domain.user.repositorys.UsersRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * 대댓글(Reply) 관련 비즈니스 로직을 처리하는 서비스 클래스입니다.
 * 대댓글 조회, 생성, 수정, 삭제 기능을 제공합니다.
 */
@Service
public class ReplyService {
    private final UsersRepository userRepository;
    private final PostCommentRepository postCommentRepository;

    /**
     * ReplyService의 생성자입니다.
     *
     * @param userRepository 사용자 데이터를 처리하는 리포지토리
     * @param postCommentRepository 댓글 데이터를 처리하는 리포지토리
     */
    public ReplyService(UsersRepository userRepository, PostCommentRepository postCommentRepository) {
        this.userRepository = userRepository;
        this.postCommentRepository = postCommentRepository;
    }

    /**
     * 특정 댓글 ID로 대댓글 목록을 조회합니다.
     *
     * @param commentId 조회할 댓글의 ID
     * @return 해당 댓글의 대댓글 목록을 포함한 요청 객체 리스트
     *
     * @throws NotFoundCommentException 댓글을 찾을 수 없을 때 발생합니다.
     */
    public List<UpdateReplyReq> getRepliesByCommentId(Long commentId) {
        if (postCommentRepository.findById(commentId).isEmpty()) {
            throw new NotFoundCommentException();
        }
        return postCommentRepository.findAllByParentComment_CommentId(commentId)
                .stream()
                .map(PostComment::toUpdateReplyReq)
                .toList();
    }

    /**
     * 새로운 대댓글을 생성합니다.
     *
     * @param reply 생성할 대댓글의 요청 정보를 포함한 객체
     * @return 생성된 대댓글의 DTO 객체
     *
     * @throws NotFoundUserException 사용자를 찾을 수 없을 때 발생합니다.
     * @throws NotFoundCommentException 댓글을 찾을 수 없을 때 발생합니다.
     */
    public ReplyDto createReply(CreateReplyReq reply) {
        if (userRepository.findById(reply.getUserId()).isEmpty()) {
            throw new NotFoundUserException();
        }
        if (postCommentRepository.findById(reply.getCommentId()).isEmpty()) {
            throw new NotFoundCommentException();
        }
        return PostComment.toReplyDto(postCommentRepository.save(PostComment.builder()
                .content(reply.getContent())
                .user(userRepository.findByUserId(reply.getUserId()).orElseThrow())
                .parentComment(postCommentRepository.findById(reply.getCommentId()).orElseThrow())
                .build()));
    }

    /**
     * 기존 대댓글을 수정합니다.
     *
     * @param replyReq 수정할 대댓글의 요청 정보를 포함한 객체
     *
     * @throws NotFoundReplyException 대댓글을 찾을 수 없을 때 발생합니다.
     * @throws NotMatchCommentException 대댓글이 속한 댓글과 요청된 댓글이 일치하지 않을 때 발생합니다.
     * @throws NotMatchUserException 대댓글 작성자와 요청된 사용자가 일치하지 않을 때 발생합니다.
     */
    public void updateReply(UpdateReplyReq replyReq) {
        if (postCommentRepository.findById(replyReq.getReplyId()).isEmpty()) {
            throw new NotFoundReplyException();
        }
        PostComment updatedReply = postCommentRepository.findById(replyReq.getReplyId()).get();
        if (!Objects.equals(updatedReply.getParentComment().getCommentId(), replyReq.getCommentId())) {
            throw new NotMatchCommentException();
        }
        if (!Objects.equals(updatedReply.getUser().getUserId(), replyReq.getUserId())) {
            throw new NotMatchUserException();
        }
        updatedReply.setContent(replyReq.getContent());
        postCommentRepository.save(updatedReply);
    }

    /**
     * 특정 대댓글을 삭제합니다.
     *
     * @param replyId 삭제할 대댓글의 ID
     *
     * @throws NotFoundReplyException 대댓글을 찾을 수 없을 때 발생합니다.
     */
    public void deleteReply(Long replyId) {
        if (postCommentRepository.findById(replyId).isEmpty()) {
            throw new NotFoundReplyException();
        }
        postCommentRepository.deleteById(replyId);
    }
}