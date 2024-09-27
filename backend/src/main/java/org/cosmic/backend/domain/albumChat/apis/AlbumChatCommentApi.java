package org.cosmic.backend.domain.albumChat.apis;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import org.cosmic.backend.domain.albumChat.applications.AlbumChatCommentService;
import org.cosmic.backend.domain.albumChat.dtos.comment.AlbumChatCommentDetail;
import org.cosmic.backend.domain.albumChat.dtos.comment.AlbumChatCommentRequest;
import org.cosmic.backend.globals.annotations.ApiCommonResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>AlbumChatCommentApi 클래스는 앨범 챗의 댓글 관리를 위한 API를 제공합니다.</p>
 *
 * <p>이 API는 댓글 조회, 생성, 수정, 삭제 기능을 제공합니다.</p>
 *
 */
@RestController
@RequestMapping("/api/")
@ApiCommonResponses
@Tag(name = "앨범 챗 관련 API", description = "앨범 챗 댓글/대댓글/좋아요 제공")
public class AlbumChatCommentApi {

    private final AlbumChatCommentService commentService;

    /**
     * <p>AlbumChatCommentApi 생성자입니다.</p>
     *
     * @param commentService 댓글 관리를 위한 서비스 클래스
     */
    public AlbumChatCommentApi(AlbumChatCommentService commentService) {
        this.commentService = commentService;
    }

    /**
     * <p>특정 앨범의 앨범챗 댓글을 조회합니다.</p>
     *
     * @param albumId 조회할 앨범의 ID
     * @param sorted 정렬 기준 (예: 좋아요 순)
     * @param count 조회할 댓글의 수
     * @return 앨범챗 댓글 목록을 포함한 {@link ResponseEntity}
     */
    @Transactional
    @GetMapping("/album/{albumId}/comment")
    @ApiResponse(responseCode = "404", description = "Not Found Album")
    @Operation(summary = "특정 앨범의 앨범챗 댓글 열기")
    public ResponseEntity<List<AlbumChatCommentDetail>> getAlbumChatComment(
            @PathVariable("albumId") Long albumId, @RequestParam String sorted, @RequestParam int count) {
        return ResponseEntity.ok(commentService.getAlbumChatComment(albumId, sorted, count));
    }

    /**
     * <p>특정 앨범에 새로운 앨범챗 댓글을 생성합니다.</p>
     *
     * @param albumId 앨범의 ID
     * @param comment 댓글 생성 요청 데이터를 포함한 객체
     * @param userId 댓글을 생성한 사용자의 ID (인증된 사용자)
     * @return 생성된 댓글 목록을 포함한 {@link ResponseEntity}
     */
    @PostMapping("/album/{albumId}/comment")
    @Transactional
    @ApiResponse(responseCode = "404", description = "Not Found User or Album")
    @Operation(summary = "특정 앨범의 앨범챗 댓글 만들기")
    public ResponseEntity<List<AlbumChatCommentDetail>> albumChatCommentCreate(
            @PathVariable Long albumId, @RequestBody AlbumChatCommentRequest comment, @AuthenticationPrincipal Long userId) {
        return ResponseEntity.ok(commentService.albumChatCommentCreate(albumId, comment, userId));
    }

    /**
     * <p>특정 앨범의 앨범챗 댓글을 수정합니다.</p>
     *
     * @param albumId 앨범의 ID
     * @param albumChatCommentId 수정할 댓글의 ID
     * @param comment 수정할 댓글 데이터를 포함한 객체
     * @param userId 댓글을 수정하는 사용자의 ID (인증된 사용자)
     * @return 수정된 댓글 목록을 포함한 {@link ResponseEntity}
     */
    @PostMapping("/album/{albumId}/comment/{albumChatCommentId}")
    @Transactional
    @ApiResponse(responseCode = "400", description = "Not Match Album or User")
    @ApiResponse(responseCode = "404", description = "Not Found AlbumChatComment Or User")
    @Operation(summary = "특정 앨범의 앨범챗 댓글 수정")
    public ResponseEntity<List<AlbumChatCommentDetail>> albumChatCommentUpdate(
            @PathVariable Long albumId, @PathVariable Long albumChatCommentId,
            @RequestBody AlbumChatCommentRequest comment, @AuthenticationPrincipal Long userId) {
        return ResponseEntity.ok(commentService.albumChatCommentUpdate(albumId, albumChatCommentId, comment, userId));
    }

    /**
     * <p>특정 앨범의 앨범챗 댓글을 삭제합니다.</p>
     *
     * @param albumId 앨범의 ID
     * @param albumChatCommentId 삭제할 댓글의 ID
     * @param sorted 정렬 기준
     * @param count 조회할 댓글의 수
     * @return 삭제 후 댓글 목록을 포함한 {@link ResponseEntity}
     */
    @DeleteMapping("/album/{albumId}/comment/{albumChatCommentId}")
    @Transactional
    @ApiResponse(responseCode = "404", description = "Not Found AlbumChatComment")
    @Operation(summary = "특정 앨범의 앨범챗 댓글 삭제")
    public ResponseEntity<List<AlbumChatCommentDetail>> albumChatCommentDelete(
            @PathVariable Long albumId, @PathVariable Long albumChatCommentId,
            @RequestParam String sorted, @RequestParam int count) {
        return ResponseEntity.ok(commentService.albumChatCommentDelete(albumId, albumChatCommentId, sorted, count));
    }
}
