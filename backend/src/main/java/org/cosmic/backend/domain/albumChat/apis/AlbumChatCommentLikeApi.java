package org.cosmic.backend.domain.albumChat.apis;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.cosmic.backend.domain.albumChat.applications.AlbumChatCommentLikeService;
import org.cosmic.backend.domain.albumChat.applications.AlbumChatCommentService;
import org.cosmic.backend.domain.albumChat.dtos.comment.AlbumChatCommentDetail;
import org.cosmic.backend.domain.albumChat.dtos.commentlike.AlbumChatCommentLikeDetail;
import org.cosmic.backend.domain.albumChat.exceptions.NotFoundAlbumChatCommentException;
import org.cosmic.backend.globals.annotations.ApiCommonResponses;
import org.cosmic.backend.globals.exceptions.NotFoundException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>AlbumChatCommentLikeApi 클래스는 앨범 챗 댓글에 대한 좋아요 기능을 처리하는 API를 정의합니다.</p>
 *
 * <p>이 API는 특정 댓글에 대한 좋아요 조회, 생성, 삭제 기능을 제공합니다.</p>
 */
@RestController
@RequestMapping("/api/")
@ApiCommonResponses
@Tag(name = "앨범 챗 관련 API", description = "앨범 챗 댓글/대댓글/좋아요 제공")
public class AlbumChatCommentLikeApi {

  private final AlbumChatCommentLikeService likeService;
  private final AlbumChatCommentService albumChatCommentService;

  /**
   * <p>AlbumChatCommentLikeApi 생성자입니다.</p>
   *
   * @param likeService 앨범 챗 댓글 좋아요 서비스를 처리하는 서비스 객체
   */
  public AlbumChatCommentLikeApi(AlbumChatCommentLikeService likeService,
      AlbumChatCommentService albumChatCommentService) {
    this.likeService = likeService;
    this.albumChatCommentService = albumChatCommentService;
  }

  /**
   * <p>특정 앨범의 앨범챗 댓글에 대한 좋아요 목록을 조회합니다.</p>
   *
   * @param albumChatCommentId 조회할 댓글 ID
   * @return 좋아요 목록을 포함한 {@link ResponseEntity}
   * @throws NotFoundAlbumChatCommentException 앨범챗 댓글을 찾을 수 없을 때 발생합니다.
   */
  @GetMapping("/album/{albumId}/comment/{albumChatCommentId}/commentLike")
  @ApiResponse(responseCode = "404", description = "Not Found AlbumChatComment")
  @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
      array = @ArraySchema(schema = @Schema(implementation = AlbumChatCommentLikeDetail.class))))
  @Operation(summary = "댓글 좋아요 조회", description = "특정 앨범의 앨범챗 댓글의 좋아요 조회")
  public ResponseEntity<List<AlbumChatCommentLikeDetail>> albumChatCommentLikeGetByAlbumChatCommentId(
      @Parameter(description = "앨범챗 댓글 id")
      @PathVariable Long albumChatCommentId) {
    return ResponseEntity.ok(
        likeService.getAlbumChatCommentLikeByAlbumChatCommentId(albumChatCommentId));
  }

  @PostMapping("/album/{spotifyAlbumId}/comment/{albumChatCommentId}/commentLike")
  @ApiResponse(responseCode = "200", content = {
      @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
          array = @ArraySchema(schema = @Schema(implementation = AlbumChatCommentDetail.class)))
  })
  @ApiResponse(responseCode = "404", description = "Not Found User or AlbumChat")
  @Operation(summary = "앨범 챗 댓글 좋아요 API", description = "앨범 챗 특정 댓글에 대해 좋아요 혹은 좋아요를 취소합니다.")
  public ResponseEntity<List<AlbumChatCommentDetail>> likeAlbumChat(
      @PathVariable String spotifyAlbumId,
      @PathVariable Long albumChatCommentId,
      @AuthenticationPrincipal Long userId,
      @Parameter(description = "댓글 정렬", required = false)
      @RequestParam(required = false, defaultValue = "recent") String sorted,
      @Parameter(description = "페이지 수")
      @RequestParam(required = false, defaultValue = "0") Integer page,
      @Parameter(description = "제공량")
      @RequestParam(required = false, defaultValue = "5") Integer limit
  ) {
    try {
      likeService.unlike(albumChatCommentId, userId);
    } catch (NotFoundException e) {
      likeService.like(albumChatCommentId, userId);
    }
    return ResponseEntity.ok(
        albumChatCommentService.getAlbumChatComment(spotifyAlbumId, sorted, page, limit));
  }


}
