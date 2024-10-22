package org.cosmic.backend.domain.post.apis;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.cosmic.backend.domain.playList.exceptions.NotFoundUserException;
import org.cosmic.backend.domain.post.applications.LikeService;
import org.cosmic.backend.domain.post.dtos.Like.LikeReq;
import org.cosmic.backend.domain.post.dtos.Like.LikeResponse;
import org.cosmic.backend.domain.post.dtos.Post.PostAndCommentsDetail;
import org.cosmic.backend.domain.post.exceptions.ExistLikeException;
import org.cosmic.backend.domain.post.exceptions.NotFoundLikeException;
import org.cosmic.backend.domain.post.exceptions.NotFoundPostException;
import org.cosmic.backend.globals.annotations.ApiCommonResponses;
import org.cosmic.backend.globals.dto.ErrorResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 좋아요 관련 API를 제공하는 REST 컨트롤러입니다.
 * 특정 게시글의 좋아요 조회, 좋아요 생성, 좋아요 삭제 기능을 제공합니다.
 */
@RestController
@RequestMapping("/api/album/post/{postId}/like")
@ApiCommonResponses
@Tag(name = "앨범 포스트 관련 API", description = "앨범 포스트 및 댓글/대댓글/좋아요")
public class LikeApi {
    private final LikeService likeService;

    /**
     * LikeApi의 생성자입니다.
     *
     * @param likeService 좋아요 관련 비즈니스 로직을 처리하는 서비스 클래스
     */
    public LikeApi(LikeService likeService) {
        this.likeService = likeService;
    }

    /**
     * 특정 게시글의 좋아요 목록을 조회합니다.
     *
     * @return 해당 게시글의 좋아요 응답 객체 리스트
     *
     * @throws NotFoundPostException 게시글을 찾을 수 없을 때 발생합니다.
     */
    @GetMapping("")
    @ApiResponse(responseCode = "404", description = "Not Found Post")
    @Operation(hidden = true)
    public ResponseEntity<List<LikeResponse>> getLikesByPostId(@PathVariable Long postId) {
        return ResponseEntity.ok(likeService.getLikesByPostId(postId));
    }

    /**
     * 새로운 좋아요를 생성합니다.
     *
     * @return 생성된 좋아요 요청 객체
     *
     * @throws NotFoundPostException 게시글을 찾을 수 없을 때 발생합니다.
     * @throws NotFoundUserException 사용자를 찾을 수 없을 때 발생합니다.
     * @throws ExistLikeException 해당 사용자가 이미 게시글에 좋아요를 남겼을 때 발생합니다.
     */
    @PostMapping("/")
    @ApiResponse(responseCode = "404", description = "Not Found User or Post")
    @ApiResponse(responseCode = "409", description = "Like Already Exists",
            content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class))
            }
    )
    @Operation(hidden = true)
    public LikeReq createLike(@PathVariable Long postId, @AuthenticationPrincipal Long userId) {
        return likeService.createLike(userId, postId);
    }

    @PostMapping("")
    @ApiResponse(responseCode = "200", content = {
            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = PostAndCommentsDetail.class))
    })
    @ApiResponse(responseCode = "404", description = "Not Found User or Post")
    @Operation(summary = "앨범 포스트 좋아요 API", description = "앨범 포스트에 대해 좋아요 혹은 좋아요를 취소합니다.")
    public ResponseEntity<PostAndCommentsDetail> likePost(@PathVariable Long postId, @AuthenticationPrincipal Long userId) {
        return ResponseEntity.ok(likeService.likeOrUnlikePost(postId, userId));
    }

    /**
     * 좋아요를 삭제합니다.
     *
     * @return 삭제 성공 메시지를 포함한 {@link ResponseEntity}
     *
     * @throws NotFoundLikeException 좋아요를 찾을 수 없을 때 발생합니다.
     */
    @DeleteMapping("")
    @ApiResponse(responseCode = "404", description = "Not Found Like")
    @Operation(hidden = true,deprecated = true)
    public ResponseEntity<?> deleteLike(@PathVariable Long postId, @AuthenticationPrincipal Long userId) {
        likeService.deleteLike(userId, postId);
        return ResponseEntity.ok("성공");
    }
}