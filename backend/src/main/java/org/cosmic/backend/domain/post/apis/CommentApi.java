package org.cosmic.backend.domain.post.apis;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.cosmic.backend.domain.playList.exceptions.NotFoundUserException;
import org.cosmic.backend.domain.post.applications.CommentService;
import org.cosmic.backend.domain.post.dtos.Comment.CommentDetail;
import org.cosmic.backend.domain.post.dtos.Comment.CreateCommentRequest;
import org.cosmic.backend.domain.post.dtos.Comment.UpdateCommentRequest;
import org.cosmic.backend.domain.post.exceptions.NotFoundCommentException;
import org.cosmic.backend.domain.post.exceptions.NotFoundPostException;
import org.cosmic.backend.domain.post.exceptions.NotMatchPostException;
import org.cosmic.backend.domain.post.exceptions.NotMatchUserException;
import org.cosmic.backend.globals.annotations.ApiCommonResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 댓글 관련 API를 제공하는 REST 컨트롤러입니다.
 * 댓글 조회, 생성, 수정, 삭제 기능을 제공합니다.
 */
@RestController
@RequestMapping("/api/album/post/{postId}/comment")
@ApiCommonResponses
public class CommentApi {
    private final CommentService commentService;

    /**
     * CommentApi의 생성자입니다.
     *
     * @param commentService 댓글 관련 비즈니스 로직을 처리하는 서비스 클래스
     */
    public CommentApi(CommentService commentService) {
        this.commentService = commentService;
    }

    /**
     * 게시글 ID로 댓글 목록을 조회합니다.
     *
     * @return 게시글에 해당하는 댓글 요청 객체 리스트
     *
     * @throws NotFoundPostException 게시글을 찾을 수 없을 때 발생합니다.
     */
    @GetMapping("/")
    @ApiResponse(responseCode = "404", description = "Not Found Post")
    public ResponseEntity<List<CommentDetail>> getCommentsByPostId(@PathVariable Long postId) {
        return ResponseEntity.ok(commentService.getCommentsByPostId(postId));
    }

    /**
     * 새로운 댓글을 생성합니다.
     *
     * @param comment 생성할 댓글의 요청 정보를 포함한 객체
     * @return 생성된 댓글의 DTO 객체
     *
     * @throws NotFoundPostException 게시글을 찾을 수 없을 때 발생합니다.
     * @throws NotFoundUserException 사용자를 찾을 수 없을 때 발생합니다.
     */
    @PostMapping("/")
    @ApiResponse(responseCode = "404", description = "Not Found User or Post")
    public ResponseEntity<List<CommentDetail>> createComment(@RequestBody CreateCommentRequest comment, @PathVariable Long postId, @AuthenticationPrincipal Long userId) {
        return ResponseEntity.ok(commentService.createComment(comment.getContent(), postId, userId));
    }

    /**
     * 기존 댓글을 수정합니다.
     *
     * @param comment 수정할 댓글의 요청 정보를 포함한 객체
     * @return 수정 성공 메시지를 포함한 {@link ResponseEntity}
     *
     * @throws NotFoundCommentException 댓글을 찾을 수 없을 때 발생합니다.
     * @throws NotMatchUserException 사용자가 댓글 작성자와 일치하지 않을 경우 발생합니다.
     * @throws NotMatchPostException 댓글이 게시글과 일치하지 않을 경우 발생합니다.
     */
    @PostMapping("/{commentId}")
    @ApiResponse(responseCode = "404", description = "Not Found Post or Comment")
    public ResponseEntity<List<CommentDetail>> updateComment(@RequestBody UpdateCommentRequest comment, @PathVariable Long commentId, @AuthenticationPrincipal Long userId) {
        return ResponseEntity.ok(commentService.updateComment(comment.getContent(), commentId, userId));
    }

    /**
     * 댓글을 삭제합니다.
     *
     * @return 삭제 성공 메시지를 포함한 {@link ResponseEntity}
     *
     * @throws NotFoundCommentException 댓글을 찾을 수 없을 때 발생합니다.
     */
    @DeleteMapping("/{commentId}")
    @ApiResponse(responseCode = "404", description = "Not Found Comment")
    public ResponseEntity<List<CommentDetail>> deleteComment(@PathVariable Long commentId, @PathVariable Long postId, @AuthenticationPrincipal Long userId) {
        return ResponseEntity.ok(commentService.deleteComment(commentId, postId, userId));
    }

    @PostMapping("/{commentId}/like")
    @ApiResponse(responseCode = "404", description = "Not Found Comment")
    public ResponseEntity<List<CommentDetail>> likeComment(@PathVariable Long commentId, @PathVariable Long postId, @AuthenticationPrincipal Long userId) {
        return ResponseEntity.ok(commentService.likeComment(commentId, postId, userId));
    }
}
