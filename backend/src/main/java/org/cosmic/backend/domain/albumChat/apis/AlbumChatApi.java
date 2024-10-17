package org.cosmic.backend.domain.albumChat.apis;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.cosmic.backend.domain.albumChat.applications.AlbumChatService;
import org.cosmic.backend.domain.albumChat.dtos.albumChat.AlbumChatDetail;
import org.cosmic.backend.domain.albumChat.exceptions.NotFoundAlbumChatException;
import org.cosmic.backend.domain.post.dtos.Post.AlbumDetail;
import org.cosmic.backend.globals.annotations.ApiCommonResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>AlbumChatApi 클래스는 앨범챗 페이지와 관련된 API를 제공합니다.</p>
 *
 * <p>이 API를 통해 사용자는 특정 앨범의 앨범챗을 열고, 좋아요 순으로 댓글을 정렬하는 등의 기능을 수행할 수 있습니다.</p>
 *
 * <p>주요 기능으로는 앨범챗 조회, 앨범챗 홈 페이지에서 많은 앨범 챗을 가져오는 기능을 포함합니다.</p>
 */
@RestController
@RequestMapping("/api/")
@ApiCommonResponses
@Tag(name = "앨범 챗 관련 API", description = "앨범 챗 댓글/대댓글/좋아요 제공")
@RequiredArgsConstructor
public class AlbumChatApi {

  private final AlbumChatService albumChatService;

  /**
   * <p>특정 앨범의 앨범챗을 조회하는 메서드입니다.</p>
   *
   * <p>사용자는 앨범 ID를 기반으로 앨범챗을 조회할 수 있습니다.</p>
   *
   * @param albumId 조회할 앨범의 ID
   * @return 조회된 앨범챗 데이터를 포함한 {@link ResponseEntity}
   * @throws NotFoundAlbumChatException 앨범챗을 찾을 수 없는 경우 예외를 발생시킵니다.
   */
  @GetMapping("/albumchat/{albumId}")
  @ApiResponse(responseCode = "404", description = "Not Found AlbumChat")
  @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(contentMediaType =
      MediaType.APPLICATION_JSON_VALUE, implementation = AlbumChatDetail.class))})
  @Operation(summary = "앨범챗 조회", description = "특정 앨범의 앨범챗 조회")
  public ResponseEntity<AlbumChatDetail> getAlbumChatById(
      @Parameter(description = "앨범id") @PathVariable("albumId") Long albumId) {
    return ResponseEntity.ok(albumChatService.getAlbumChatById(albumId));
  }

  /**
   * <p>앨범챗 홈 페이지에서 앨범 챗이 많은 순으로 앨범 정보를 조회하는 메서드입니다.</p>
   *
   * <p>사용자는 페이지와 제공량을 설정하여, 좋아요나 댓글이 많은 순으로 정렬된 앨범 정보를 가져옵니다.</p>
   *
   * @param page  조회할 페이지 번호
   * @param limit 페이지당 조회할 앨범의 수
   * @return 조회된 앨범 챗 데이터를 포함한 {@link ResponseEntity} 리스트
   */
  @GetMapping("/albumchat")
  @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
      array = @ArraySchema(schema = @Schema(implementation = AlbumDetail.class))))
  @Operation(summary = "앨범챗홈 페이지", description = "앨범 챗 많은 순으로 album정보 띄우기")
  public ResponseEntity<List<AlbumDetail>> albumChatHome(
      @Parameter(description = "페이지 수") @RequestParam Integer page,
      @Parameter(description = "제공량") @RequestParam Integer limit) {
    return ResponseEntity.ok(albumChatService.albumChatHome(page, limit));
  }
}
