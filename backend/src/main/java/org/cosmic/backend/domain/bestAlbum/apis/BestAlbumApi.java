package org.cosmic.backend.domain.bestAlbum.apis;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.cosmic.backend.domain.bestAlbum.applications.BestAlbumService;
import org.cosmic.backend.domain.bestAlbum.dtos.AlbumScoreDto;
import org.cosmic.backend.domain.bestAlbum.dtos.BestAlbumDetail;
import org.cosmic.backend.domain.bestAlbum.dtos.BestAlbumListRequest;
import org.cosmic.backend.globals.annotations.ApiCommonResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p> BestAlbumApi는 사용자의 좋아요 앨범 목록을 관리하고, 앨범과 아티스트를 검색하는 API 엔드포인트를 제공합니다. </p>
 *
 * <p> 이 컨트롤러는 다음과 같은 기능을 제공합니다: </p>
 * <ul>
 *     <li>사용자의 좋아요 앨범 목록 조회</li>
 *     <li>사용자에게 앨범 추가</li>
 *     <li>좋아요 앨범 목록 저장 또는 순서 수정</li>
 *     <li>아티스트 및 앨범 검색</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/")
@ApiCommonResponses
@Tag(name = "베스트 앨범 관련 API", description = "베스트 앨범 정보 제공 및 저장")
@RequiredArgsConstructor
public class BestAlbumApi {

  private final BestAlbumService bestAlbumService;

  /**
   * <p>특정 유저의 베스트 앨범 목록을 제공합니다.</p>
   *
   * @param userId 유저의 ID
   * @return 유저의 베스트 앨범 목록
   */
  @Transactional
  @GetMapping("/user/{userId}/bestAlbum")
  @ApiResponse(responseCode = "404", description = "Not Found User")
  @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
      array = @ArraySchema(schema = @Schema(implementation = BestAlbumDetail.class))))
  @Operation(summary = "특정 유저의 베스트 앨범 조회", description = "특정 유저의 베스트 앨범 정보를 조회합니다.")
  public ResponseEntity<List<BestAlbumDetail>> bestAlbumGive(
      @Parameter(description = "유저 id")
      @PathVariable Long userId) {
    return ResponseEntity.ok(bestAlbumService.open(userId));
  }

  /**
   * <p>특정 유저에게 베스트 앨범을 추가합니다.</p>
   *
   * @param albumScoreDto  추가할 앨범에 대한 점수 정보
   * @param spotifyAlbumId 추가할 앨범의 ID
   * @param userId         유저의 ID
   * @return 업데이트된 베스트 앨범 목록
   */
  @Transactional
  @PostMapping("/bestAlbum/{spotifyAlbumId}")
  @ApiResponse(responseCode = "404", description = "Not Found User or Album")
  @ApiResponse(responseCode = "409", description = "Exist BestAlbum")
  @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
      array = @ArraySchema(schema = @Schema(implementation = BestAlbumDetail.class))))
  @Operation(summary = "특정 유저에게 베스트 앨범 1개 추가", description = "특정 유저의 베스트앨범 리스트에 1개 앨범을 추가합니다.")
  public ResponseEntity<List<BestAlbumDetail>> bestAlbumAdd(
      @Parameter(description = "별점")
      @RequestBody AlbumScoreDto albumScoreDto,
      @Parameter(description = "추가할 앨범id")
      @PathVariable String spotifyAlbumId,
      @AuthenticationPrincipal Long userId
  ) {
    return ResponseEntity.ok(
        bestAlbumService.add(albumScoreDto.getScore(), userId, spotifyAlbumId));
  }

  /**
   * <p>특정 유저의 베스트 앨범 정보 순서를 수정하거나 저장합니다.</p>
   *
   * @param bestAlbumlistRequest 수정할 앨범 목록의 요청 정보
   * @param userId               유저의 ID
   * @return 수정된 베스트 앨범 목록
   */
  @Transactional
  @PostMapping("/bestAlbum")
  @ApiResponse(responseCode = "400", description = "Not Match BestAlbum")
  @ApiResponse(responseCode = "404", description = "Not Found User or Album")
  @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
      array = @ArraySchema(schema = @Schema(implementation = BestAlbumDetail.class))))
  @Operation(summary = "베스트 앨범 수정", description = "특정 유저의 베스트 앨범 정보 순서 수정 또는 저장")
  public ResponseEntity<List<BestAlbumDetail>> bestAlbumSave(
      @Parameter(description = "베스트앨범 리스트")
      @RequestBody BestAlbumListRequest bestAlbumlistRequest,
      @AuthenticationPrincipal Long userId) {
    return ResponseEntity.ok(bestAlbumService.save(userId, bestAlbumlistRequest.getBestalbum()));
  }
}
