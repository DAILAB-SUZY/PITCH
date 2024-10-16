package org.cosmic.backend.domain.albumChat.apis;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.cosmic.backend.domain.albumChat.applications.AlbumChatCommentLikeService;
import org.cosmic.backend.domain.albumChat.dtos.commentlike.AlbumChatCommentLikeDetail;
import org.cosmic.backend.domain.post.dtos.Post.PostAndCommentsDetail;
import org.cosmic.backend.globals.annotations.ApiCommonResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import org.cosmic.backend.domain.albumChat.exceptions.ExistCommentLikeException;
import org.cosmic.backend.domain.albumChat.exceptions.NotFoundAlbumChatCommentException;
import org.cosmic.backend.domain.albumChat.exceptions.NotFoundCommentLikeException;
import org.cosmic.backend.domain.playList.exceptions.NotFoundUserException;
import java.util.List;

/**
 * <p>AlbumChatCommentLikeApi 클래스는 앨범 챗 댓글에 대한 좋아요 기능을 처리하는 API를 정의합니다.</p>
 *
 * <p>이 API는 특정 댓글에 대한 좋아요 조회, 생성, 삭제 기능을 제공합니다.</p>
 *
 */
@RestController
@RequestMapping("/api/")
@ApiCommonResponses
@Tag(name = "앨범 챗 관련 API", description = "앨범 챗 댓글/대댓글/좋아요 제공")
public class AlbumChatCommentLikeApi {

    private final AlbumChatCommentLikeService likeService;

    /**
     * <p>AlbumChatCommentLikeApi 생성자입니다.</p>
     *
     * @param likeService 앨범 챗 댓글 좋아요 서비스를 처리하는 서비스 객체
     */
    public AlbumChatCommentLikeApi(AlbumChatCommentLikeService likeService) {
        this.likeService = likeService;
    }

    /**
     * <p>특정 앨범의 앨범챗 댓글에 대한 좋아요 목록을 조회합니다.</p>
     *
     * @param albumChatCommentId 조회할 댓글 ID
     * @return 좋아요 목록을 포함한 {@link ResponseEntity}
     *
     * @throws NotFoundAlbumChatCommentException 앨범챗 댓글을 찾을 수 없을 때 발생합니다.
     */
    @GetMapping("/album/{albumId}/comment/{albumChatCommentId}/commentLike")
    @ApiResponse(responseCode = "404", description = "Not Found AlbumChatComment")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
            array = @ArraySchema(schema = @Schema(implementation = AlbumChatCommentLikeDetail.class))))
    @Operation(summary = "댓글 좋아요 조회",description = "특정 앨범의 앨범챗 댓글의 좋아요 조회")
    public ResponseEntity<List<AlbumChatCommentLikeDetail>> albumChatCommentLikeGetByAlbumChatCommentId(
            @Parameter(description = "앨범챗 댓글 id")
            @PathVariable Long albumChatCommentId) {
        return ResponseEntity.ok(likeService.getAlbumChatCommentLikeByAlbumChatCommentId(albumChatCommentId));
    }

    @PostMapping("/album/{albumId}/comment/{albumChatCommentId}/commentLike")
    @ApiResponse(responseCode = "200", content = {
            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = PostAndCommentsDetail.class))
    })
    @ApiResponse(responseCode = "404", description = "Not Found User or AlbumChat")
    @Operation(summary = "앨범 챗 댓글 좋아요 API", description = "앨범 챗 특정 댓글에 대해 좋아요 혹은 좋아요를 취소합니다.")
    public ResponseEntity<List<AlbumChatCommentLikeDetail>> likeAlbumChat(@PathVariable Long albumId,@PathVariable Long albumChatCommentId, @AuthenticationPrincipal Long userId) {
        return ResponseEntity.ok(likeService.likeOrUnlikeAlbumChat(albumChatCommentId, userId));
    }



}
