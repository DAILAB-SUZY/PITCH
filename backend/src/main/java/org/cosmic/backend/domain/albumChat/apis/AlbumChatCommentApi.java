package org.cosmic.backend.domain.albumChat.apis;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import java.util.List;
import org.cosmic.backend.domain.albumChat.applications.AlbumChatCommentService;
import org.cosmic.backend.domain.albumChat.dtos.comment.AlbumChatCommentDetail;
import org.cosmic.backend.domain.albumChat.dtos.comment.AlbumChatCommentRequest;
import org.cosmic.backend.domain.playList.dtos.PlaylistAndRecommendDetail;
import org.cosmic.backend.globals.annotations.ApiCommonResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>AlbumChatCommentApi 클래스는 앨범 챗의 댓글 관리를 위한 API를 제공합니다.</p>
 *
 * <p>이 API는 댓글 조회, 생성, 수정, 삭제 기능을 제공합니다.</p>
 */
@RestController
@RequestMapping("/api/album")
@ApiCommonResponses
@Tag(name = "앨범 챗 관련 API", description = "앨범 챗 댓글/대댓글/좋아요 제공")
@ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
    array = @ArraySchema(schema = @Schema(implementation = AlbumChatCommentDetail.class))))
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
   * @param spotifyAlbumId 조회할 앨범의 ID
   * @param sorted         정렬 기준 (예: 좋아요 순)
   * @return 앨범챗 댓글 목록을 포함한 {@link ResponseEntity}
   */
  @Transactional
  @GetMapping("/{spotifyAlbumId}/albumchat")
  @ApiResponse(responseCode = "404", description = "Not Found Album")
  @Operation(summary = "댓글 조회", description = "특정 앨범의 앨범챗 댓글 조회, sorted가 'recent'인 경우 최신순으로 정렬하고 그 외에는 album_like 순으로 정렬됩니다.")
  public ResponseEntity<List<AlbumChatCommentDetail>> getAlbumChatComment(
      @Parameter(description = "앨범 id")
      @PathVariable("spotifyAlbumId") String spotifyAlbumId,
      @Parameter(description = "댓글 정렬", required = false)
      @RequestParam(required = false, defaultValue = "recent") String sorted,
      @Parameter(description = "페이지 수")
      @RequestParam(required = false, defaultValue = "0") Integer page,
      @Parameter(description = "제공량")
      @RequestParam(required = false, defaultValue = "5") Integer limit
  ) {
    return ResponseEntity.ok(
        commentService.getAlbumChatComment(spotifyAlbumId, sorted, page, limit));
  }

  /**
   * <p>특정 앨범에 새로운 앨범챗 댓글을 생성합니다.</p>
   *
   * @param spotifyAlbumID 앨범의 ID
   * @param comment        댓글 생성 요청 데이터를 포함한 객체
   * @param userId         댓글을 생성한 사용자의 ID (인증된 사용자)
   * @return 생성된 댓글 목록을 포함한 {@link ResponseEntity}
   */
  @PostMapping("/{spotifyAlbumID}/albumchat")
  @Transactional
  @ApiResponse(responseCode = "404", description = "Not Found User or Album")
  @Operation(summary = "댓글 생성", description = "특정 앨범챗 댓글 생성, content와 대댓글인 경우 parentAlbumChatCommentId를 채워주세요. parentAlbumChatCommentId는 albumchatComment의 ID입니다.sorted가 'recent'인 경우 최신순으로 정렬하고 그 외에는 album_like 순으로 정렬됩니다.")
  public ResponseEntity<List<AlbumChatCommentDetail>> albumChatCommentCreate(
      @Parameter(description = "앨범id")
      @PathVariable String spotifyAlbumID,
      @Parameter(description = "앨범챗 댓글 내용")
      @RequestBody AlbumChatCommentRequest comment,
      @AuthenticationPrincipal Long userId) {
    return ResponseEntity.ok(
        commentService.albumChatCommentCreate(spotifyAlbumID, comment, userId));
  }

  /**
   * <p>특정 앨범의 앨범챗 댓글을 수정합니다.</p>
   *
   * @param spotifyAlbumId 앨범의 ID
   * @param albumChatId    수정할 댓글의 ID
   * @param comment        수정할 댓글 데이터를 포함한 객체
   * @param userId         댓글을 수정하는 사용자의 ID (인증된 사용자)
   * @return 수정된 댓글 목록을 포함한 {@link ResponseEntity}
   */
  @PostMapping("/{spotifyAlbumId}/albumchat/{albumChatId}")
  @Transactional
  @ApiResponse(responseCode = "400", description = "Not Match Album or User")
  @ApiResponse(responseCode = "404", description = "Not Found AlbumChatComment Or User")
  @Operation(summary = "댓글 수정", description = "특정 앨범챗 댓글 수정, request body에 content만 채워서 넣으면 됨. sorted가 'recent'인 경우 최신순으로 정렬하고 그 외에는 album_like 순으로 정렬됩니다.")
  public ResponseEntity<List<AlbumChatCommentDetail>> albumChatCommentUpdate(
      @Parameter(description = "앨범 id")
      @PathVariable String spotifyAlbumId,
      @Parameter(description = "앨범챗 댓글 id")
      @PathVariable Long albumChatId,
      @Parameter(description = "앨범챗 댓글 내용")
      @RequestBody AlbumChatCommentRequest comment,
      @AuthenticationPrincipal Long userId) {
    return ResponseEntity.ok(
        commentService.albumChatCommentUpdate(spotifyAlbumId, albumChatId, comment, userId));
  }

  /**
   * <p>특정 앨범의 앨범챗 댓글을 삭제합니다.</p>
   *
   * @param spotifyAlbumId 앨범의 ID
   * @param albumChatId    삭제할 댓글의 ID
   * @param sorted         정렬 기준
   * @return 삭제 후 댓글 목록을 포함한 {@link ResponseEntity}
   */
  @DeleteMapping("/{spotifyAlbumId}/albumchat/{albumChatId}")
  @Transactional
  @ApiResponse(responseCode = "404", description = "Not Found AlbumChatComment")
  @Operation(summary = "특정 앨범의 앨범챗 댓글 삭제, sorted가 'recent'인 경우 최신순으로 정렬하고 그 외에는 album_like 순으로 정렬됩니다.")
  public ResponseEntity<List<AlbumChatCommentDetail>> albumChatCommentDelete(
      @Parameter(description = "앨범 id")
      @PathVariable String spotifyAlbumId,
      @Parameter(description = "앨범챗 댓글 id")
      @PathVariable Long albumChatId,
      @Parameter(description = "댓글 정렬", required = false)
      @RequestParam String sorted) {
    return ResponseEntity.ok(
        commentService.albumChatCommentDelete(spotifyAlbumId, albumChatId, sorted));
  }

  @GetMapping("/{spotifyAlbumId}/albumchat/{albumChatId}")
  @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
          schema = @Schema(implementation = AlbumChatCommentDetail.class)))
  @ApiResponse(responseCode = "404", description = "Not Found AlbumChatComment")
  @Operation(summary = "특정 앨범의 앨범챗 댓글만 가져오기, sorted가 'recent'인 경우 최신순으로 정렬하고 그 외에는 album_like 순으로 정렬됩니다.")
  public ResponseEntity<AlbumChatCommentDetail> albumChatCommentGet(
          @Parameter(description = "앨범 id")
          @PathVariable String spotifyAlbumId,
          @Parameter(description = "앨범챗 댓글 id")
          @PathVariable Long albumChatId,
          @Parameter(description = "댓글 정렬", required = false)
          @RequestParam String sorted) {
      return ResponseEntity.ok(
          commentService.albumChatCommentGet(spotifyAlbumId, albumChatId, sorted));
  }

}
