package org.cosmic.backend.domain.albumChat.apis;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.transaction.Transactional;
import org.cosmic.backend.domain.albumChat.applications.AlbumChatCommentService;
import org.cosmic.backend.domain.albumChat.dtos.comment.AlbumChatCommentDetail;
import org.cosmic.backend.domain.albumChat.dtos.comment.AlbumChatCommentRequest;
import org.cosmic.backend.domain.albumChat.exceptions.NotFoundAlbumChatCommentException;
import org.cosmic.backend.domain.albumChat.exceptions.NotFoundAlbumChatException;
import org.cosmic.backend.domain.albumChat.exceptions.NotMatchAlbumChatException;
import org.cosmic.backend.domain.playList.exceptions.NotFoundUserException;
import org.cosmic.backend.domain.post.exceptions.NotMatchUserException;
import org.cosmic.backend.globals.annotations.ApiCommonResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * AlbumChatCommentApi 클래스는 앨범 챗의 댓글 관리를 위한 API를 제공합니다.
 * 댓글 조회, 생성, 수정, 삭제 기능을 제공합니다.
 */
@RestController
@RequestMapping("/api/")
@ApiCommonResponses
public class AlbumChatCommentApi {
    private final AlbumChatCommentService commentService;

    /**
     * AlbumChatCommentApi 생성자.
     *
     * @param commentService 댓글 서비스 주입
     */
    public AlbumChatCommentApi(AlbumChatCommentService commentService) {
        this.commentService = commentService;
    }

    /**
     * 특정 앨범 챗 ID로 댓글 목록을 조회하는 API.
     *
     * @param albumId 조회할 앨범 챗 ID를 포함한 DTO
     * @return List<AlbumChatCommentResponse> 조회된 댓글 목록
     * @throws NotFoundAlbumChatException 앨범 챗이 존재하지 않을 경우 발생
     */
    @GetMapping("/album/{albumId}/comment")
    @Transactional
    @ApiResponse(responseCode = "404", description = "Not Found Album")
    public List<AlbumChatCommentDetail> getCommentByAlbumChatId(@PathVariable Long albumId) {
        return commentService.getCommentsByAlbumId(albumId);
    }

    /**
     * 새로운 앨범 챗 댓글을 생성하는 API
     *
     * @param comment 생성할 댓글 정보가 담긴 DTO
     * @return AlbumChatCommentDto 생성된 댓글 정보
     * @throws NotFoundAlbumChatException 앨범 챗이 존재하지 않을 경우 발생
     * @throws NotFoundUserException 사용자가 존재하지 않을 경우 발생
     */
    @PostMapping("/album/{albumId}/comment")
    @Transactional
    @ApiResponse(responseCode = "404", description = "Not Found User or Album")
    public ResponseEntity<List<AlbumChatCommentDetail>> albumChatCommentCreate(@PathVariable Long albumId,
       @RequestBody AlbumChatCommentRequest comment, @AuthenticationPrincipal Long userId) {
        System.out.println("********하이요"+commentService.albumChatCommentCreate(albumId,comment,userId).get(0).getCreateAt());
        return ResponseEntity.ok(commentService.albumChatCommentCreate(albumId,comment,userId));
    }

    /**
     * 기존의 앨범 챗 댓글을 수정하는 API
     *
     * @param comment 수정할 댓글 정보가 담긴 DTO
     * @return ResponseEntity<?> 성공 메시지 반환
     * @throws NotMatchAlbumChatException 앨범 챗 ID가 일치하지 않을 경우 발생
     * @throws NotMatchUserException 수정하려는 사용자와 기존 댓글 생성한 사용자가 다를때 발생
     * @throws NotFoundAlbumChatCommentException 댓글이 존재하지 않을 경우 발생
     * @throws NotFoundUserException 사용자가 존재하지 않을 경우 발생
     */
    @PostMapping("/album/{albumId}/comment/{albumChatCommentId}")
    @Transactional
    @ApiResponse(responseCode = "400", description = "Not Match Album or User")
    @ApiResponse(responseCode = "404", description = "Not Found AlbumChatComment Or User")
    public ResponseEntity<List<AlbumChatCommentDetail>> albumChatCommentUpdate(@PathVariable Long albumId, @PathVariable Long albumChatCommentId
        , @RequestBody AlbumChatCommentRequest comment, @AuthenticationPrincipal Long userId) {
        return ResponseEntity.ok(commentService.albumChatCommentUpdate(albumId,albumChatCommentId,comment,userId));
    }

    /**
     * 앨범 챗 댓글을 삭제하는 API
     *
     * @param albumChatCommentId 삭제할 댓글 정보가 담긴 DTO
     * @return ResponseEntity<?> 성공 메시지 반환
     * @throws NotFoundAlbumChatCommentException 댓글이 존재하지 않을 경우 발생
     */
    @DeleteMapping("/album/{albumId}/comment/{albumChatCommentId}")
    @Transactional
    @ApiResponse(responseCode = "404", description = "Not Found AlbumChatComment")
    //TODO NOTMATCHUSER오류 추가
    public ResponseEntity<List<AlbumChatCommentDetail>> albumChatCommentDelete(@PathVariable Long albumChatCommentId) {
        return ResponseEntity.ok(commentService.albumChatCommentDelete(albumChatCommentId));
    }
}