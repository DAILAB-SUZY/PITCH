package org.cosmic.backend.domain.albumChat.apis;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.cosmic.backend.domain.albumChat.applications.AlbumChatCommentLikeService;
import org.cosmic.backend.domain.albumChat.dtos.commentlike.AlbumChatCommentLikeDetail;
import org.cosmic.backend.domain.albumChat.exceptions.ExistCommentLikeException;
import org.cosmic.backend.domain.albumChat.exceptions.NotFoundAlbumChatCommentException;
import org.cosmic.backend.domain.albumChat.exceptions.NotFoundCommentLikeException;
import org.cosmic.backend.domain.playList.exceptions.NotFoundUserException;
import org.cosmic.backend.globals.annotations.ApiCommonResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * AlbumChatCommentLikeApi 클래스는 앨범 챗 댓글에 대한 좋아요 기능을 처리하는 API를 정의합니다.
 */
@RestController
@RequestMapping("/api/")
@ApiCommonResponses
public class AlbumChatCommentLikeApi {//댓글 마다의 좋아요
    private final AlbumChatCommentLikeService likeService;

    /**
     * AlbumChatCommentLikeApi 생성자.
     *
     * @param likeService 앨범 챗 댓글 좋아요 서비스 객체 주입
     */
    public AlbumChatCommentLikeApi(AlbumChatCommentLikeService likeService) {
        this.likeService = likeService;
    }

    /**
     * 특정 앨범 챗 댓글에 대한 좋아요 목록을 조회하는 API
     *
     * @param albumChatCommentId 조회할 앨범 챗 댓글 정보
     * @return List<AlbumChatCommentLikeResponse> 좋아요 목록 반환
     * @throws NotFoundAlbumChatCommentException 앨범 챗 댓글이 존재하지 않을 경우 발생
     */
    @GetMapping("/album/{albumId}/comment/{albumChatCommentId}/commentLike")
    @ApiResponse(responseCode = "404", description = "Not Found AlbumChatComment")
    public ResponseEntity<List<AlbumChatCommentLikeDetail>> albumChatCommentLikeGetByAlbumChatCommentId
        (@PathVariable Long albumChatCommentId) {
        return ResponseEntity.ok(likeService.getAlbumChatCommentLikeByAlbumChatCommentId(albumChatCommentId));
    }

    /**
     * 특정 앨범 챗 댓글에 새로운 좋아요를 생성하는 API
     *
     * @param albumChatCommentId 좋아요를 생성할 댓글과 사용자 정보
     * @return AlbumChatCommentLikeIdResponse 생성된 좋아요 ID를 담은 응답 객체
     * @throws NotFoundAlbumChatCommentException 앨범 챗 댓글이 존재하지 않을 경우 발생
     * @throws NotFoundUserException 사용자가 존재하지 않을 경우 발생
     * @throws ExistCommentLikeException 이미 해당 사용자가 해당 댓글에 좋아요를 눌렀을 경우 발생
     */
    @PostMapping("/album/{albumId}/comment/{albumChatCommentId}/commentLike")
    @ApiResponse(responseCode = "404", description = "Not Found User or AlbumChatComment")
    @ApiResponse(responseCode = "409", description = "CommentLike Already Exists")
    public ResponseEntity<List<AlbumChatCommentLikeDetail>> albumChatCommentLikeCreate(
        @PathVariable Long albumChatCommentId, @AuthenticationPrincipal Long userId) {
        return ResponseEntity.ok(likeService.albumChatCommentLikeCreate(userId,albumChatCommentId));
    }

    /**
     * 특정 좋아요 ID로 좋아요를 삭제하는 API
     *
     * @param albumChatCommentId 삭제할 좋아요 ID 정보
     * @return ResponseEntity<?> 성공 메시지 반환
     * @throws NotFoundCommentLikeException 좋아요가 존재하지 않을 경우 발생
     */
    @DeleteMapping("/album/{albumId}/comment/{albumChatCommentId}/commentLike")
    @ApiResponse(responseCode = "404", description = "Not Found CommentLike")
    public ResponseEntity<List<AlbumChatCommentLikeDetail>> albumChatCommentLikeDelete(
        @PathVariable Long albumChatCommentId,@AuthenticationPrincipal Long userId) {
        return ResponseEntity.ok(likeService.albumChatCommentLikeDelete(albumChatCommentId,userId));
    }//몇개인지만 준다.
}

