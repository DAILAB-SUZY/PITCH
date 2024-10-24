package org.cosmic.backend.domain.playList.apis;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.cosmic.backend.domain.playList.applications.PlaylistService;
import org.cosmic.backend.domain.playList.dtos.FollowerPlaylistDetail;
import org.cosmic.backend.domain.playList.dtos.PlaylistAndRecommendDetail;
import org.cosmic.backend.domain.playList.dtos.PlaylistDetail;
import org.cosmic.backend.domain.playList.dtos.SpotifyTracksDto;
import org.cosmic.backend.domain.playList.exceptions.NotFoundTrackException;
import org.cosmic.backend.domain.playList.exceptions.NotFoundUserException;
import org.cosmic.backend.domain.post.dtos.Post.AlbumDetail;
import org.cosmic.backend.domain.search.applications.SearchService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

/**
 * <p>플레이리스트 관련 API를 제공하는 REST 컨트롤러입니다.</p>
 *
 * <p>이 API는 사용자 플레이리스트의 조회, 저장 및 아티스트/트랙 검색 기능을 제공합니다.</p>
 */
@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
@Tag(name = "Playlist 관련 API", description = "Playlist 노래 저장 및 제공")
public class PlaylistApi {

  private final PlaylistService playlistService;
  private final SearchService searchService;

  /**
   * <p>특정 사용자의 플레이리스트 데이터를 조회합니다.</p>
   *
   * @param userId 사용자 ID
   * @return 플레이리스트 데이터를 포함한 리스트
   * @throws NotFoundUserException 사용자를 찾을 수 없을 때 발생합니다.
   */
  @GetMapping("/user/{userId}/playlist")
  @ApiResponse(responseCode = "404", description = "Not Found User")
  @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
      schema = @Schema(implementation = PlaylistAndRecommendDetail.class)))
  @Operation(summary = "플레이리스트 조회 API", description = "특정 유저의 플레이리스트 및 추천곡 조회")
  public ResponseEntity<PlaylistAndRecommendDetail> dataGive(
      @Parameter(description = "유저 id")
      @PathVariable Long userId) {
    List<PlaylistDetail> recommendations;
    try {
      recommendations = searchService.getRecommendations(userId);
    } catch (HttpClientErrorException e) {
      recommendations = playlistService.recommendation();
    }
    return ResponseEntity.ok(new PlaylistAndRecommendDetail(playlistService.open(userId),
        recommendations));
  }

  @GetMapping("/playlist/following")
  @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
      array = @ArraySchema(schema = @Schema(implementation = FollowerPlaylistDetail.class))))
  @Operation(summary = "팔로우한 상대들의 플레이리스트 API", description = "팔로우한 유저들의 플레이리스트를 제공하는 API입니다.")
  public ResponseEntity<List<FollowerPlaylistDetail>> followingPlaylist(
      @AuthenticationPrincipal Long userId) {
    return ResponseEntity.ok(playlistService.followingPlaylists(userId));
  }

  /**
   * <p>사용자의 플레이리스트 데이터를 저장합니다.</p>
   *
   * @param playlist 저장할 플레이리스트 데이터를 포함한 DTO 객체
   * @param userId   사용자 ID (인증된 사용자)
   * @return 저장된 플레이리스트 데이터를 포함한 리스트
   * @throws NotFoundUserException  사용자를 찾을 수 없을 때 발생합니다.
   * @throws NotFoundTrackException 트랙을 찾을 수 없을 때 발생합니다.
   */
  @PostMapping("/playlist")
  @ApiResponse(responseCode = "404", description = "Not Found User or Track")
  @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
      array = @ArraySchema(schema = @Schema(implementation = PlaylistDetail.class))))
  @Operation(summary = "플레이리스트 저장", description = "특정 유저 플레이리스트 데이터 저장")

  public ResponseEntity<List<PlaylistDetail>> savePlaylistData(
      @Parameter(description = "플레이리스트에 담을 데이터 리스트")
      @RequestBody SpotifyTracksDto playlist,
      @AuthenticationPrincipal Long userId) {
    return ResponseEntity.ok(playlistService.save(userId, playlist));
  }

  @Tag(name = "앨범 챗 관련 API", description = "앨범 챗 댓글/대댓글/좋아요 제공")
  @GetMapping("/album/{spotifyAlbumId}")
  @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
      schema = @Schema(implementation = AlbumDetail.class)))
  @Operation(summary = "앨범 디테일 제공 API", description = "스포티파이 앨범 ID를 통해 앨범 디테일 정보를 제공")
  public ResponseEntity<AlbumDetail> getAlbumDetail(
      @Parameter(description = "Spotify에서 사용하는 Album ID")
      @PathVariable String spotifyAlbumId) {
    return ResponseEntity.ok(playlistService.getAlbumDetail(spotifyAlbumId));
  }
}
