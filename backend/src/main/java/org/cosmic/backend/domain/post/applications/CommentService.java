package org.cosmic.backend.domain.post.applications;

import org.cosmic.backend.domain.playList.exceptions.NotFoundUserException;
import org.cosmic.backend.domain.post.dtos.Comment.CommentDto;
import org.cosmic.backend.domain.post.dtos.Comment.CommentReq;
import org.cosmic.backend.domain.post.dtos.Comment.CreateCommentReq;
import org.cosmic.backend.domain.post.dtos.Comment.UpdateCommentReq;
import org.cosmic.backend.domain.post.entities.Comment;
import org.cosmic.backend.domain.post.exceptions.NotFoundCommentException;
import org.cosmic.backend.domain.post.exceptions.NotFoundPostException;
import org.cosmic.backend.domain.post.exceptions.NotMatchPostException;
import org.cosmic.backend.domain.post.exceptions.NotMatchUserException;
import org.cosmic.backend.domain.post.repositories.CommentRepository;
import org.cosmic.backend.domain.post.repositories.PostRepository;
import org.cosmic.backend.domain.user.repositorys.UsersRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

/**
 * 댓글과 관련된 비즈니스 로직을 처리하는 서비스 클래스입니다.
 * 댓글 조회, 생성, 수정, 삭제 기능을 제공합니다.
 */
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UsersRepository userRepository;
    private final PostRepository postRepository;

    /**
     * CommentService의 생성자입니다.
     *
     * @param commentRepository 댓글 데이터를 처리하는 리포지토리
     * @param userRepository 사용자 데이터를 처리하는 리포지토리
     * @param postRepository 게시글 데이터를 처리하는 리포지토리
     */
    public CommentService(CommentRepository commentRepository, UsersRepository userRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    /**
     * 주어진 게시글 ID로 댓글 목록을 조회합니다.
     *
     * @param postId 조회할 게시글의 ID
     * @return 게시글에 해당하는 댓글 요청 객체 리스트
     *
     * @throws NotFoundPostException 게시글이 존재하지 않을 경우 발생합니다.
     */
    public List<CommentReq> getCommentsByPostId(Long postId) {
        if (postRepository.findById(postId).isEmpty()) {
            throw new NotFoundPostException();
        }
        return commentRepository.findByPost_PostId(postId).stream().map(Comment::toCommentReq).toList();
    }

    /**
     * 새로운 댓글을 생성합니다.
     *
     * @param commentReq 생성할 댓글의 요청 정보를 포함한 객체
     * @return 생성된 댓글의 DTO 객체 {@link CommentDto}
     *
     * @throws NotFoundPostException 게시글이 존재하지 않을 경우 발생합니다.
     * @throws NotFoundUserException 사용자가 존재하지 않을 경우 발생합니다.
     */
    public CommentDto createComment(CreateCommentReq commentReq) {
        if (postRepository.findById(commentReq.getPostId()).isEmpty()) {
            throw new NotFoundPostException();
        }
        if (userRepository.findById(commentReq.getUserId()).isEmpty()) {
            throw new NotFoundUserException();
        }
        return Comment.toCommentDto(commentRepository.save(Comment.builder()
                .content(commentReq.getContent())
                .updateTime(Instant.now())
                .user(userRepository.findById(commentReq.getUserId()).get())
                .post(postRepository.findById(commentReq.getPostId()).get()).build()));
    }

    /**
     * 기존 댓글을 수정합니다.
     *
     * @param commentReq 수정할 댓글의 요청 정보를 포함한 객체
     *
     * @throws NotFoundCommentException 댓글이 존재하지 않을 경우 발생합니다.
     * @throws NotMatchUserException 사용자가 댓글 작성자와 일치하지 않을 경우 발생합니다.
     * @throws NotMatchPostException 댓글이 게시글과 일치하지 않을 경우 발생합니다.
     */
    public void updateComment(UpdateCommentReq commentReq) {
        if (commentRepository.findById(commentReq.getCommentId()).isEmpty()) {
            throw new NotFoundCommentException();
        }
        Comment comment = commentRepository.findById(commentReq.getCommentId()).get();
        if (!Objects.equals(comment.getUser().getUserId(), commentReq.getUserId())) {
            throw new NotMatchUserException();
        }
        if (!Objects.equals(comment.getPost().getPostId(), commentReq.getPostId())) {
            throw new NotMatchPostException();
        }
        comment.setContent(commentReq.getContent());
        commentRepository.save(comment);
    }

    /**
     * 댓글을 삭제합니다.
     *
     * @param commentdto 삭제할 댓글의 DTO 객체
     *
     * @throws NotFoundCommentException 댓글이 존재하지 않을 경우 발생합니다.
     */
    public void deleteComment(CommentDto commentdto) {
        if (commentRepository.findById(commentdto.getCommentId()).isEmpty()) {
            throw new NotFoundCommentException();
        }
        commentRepository.deleteById(commentdto.getCommentId());
    }
}
