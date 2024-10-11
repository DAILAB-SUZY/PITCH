package org.cosmic.backend.domain.playList.apis;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import java.util.List;
import org.cosmic.backend.domain.playList.applications.PlaylistService;
import org.cosmic.backend.domain.playList.dtos.FollowerPlaylistDetail;
import org.cosmic.backend.domain.playList.dtos.PlaylistDetail;
import org.cosmic.backend.domain.playList.dtos.PlaylistDto;
import org.cosmic.backend.domain.playList.dtos.TrackDetail;
import org.cosmic.backend.domain.playList.exceptions.NotFoundArtistException;
import org.cosmic.backend.domain.playList.exceptions.NotFoundTrackException;
import org.cosmic.backend.domain.playList.exceptions.NotFoundUserException;
import org.cosmic.backend.domain.user.repositorys.UsersRepository;
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
 * <p>플레이리스트 관련 API를 제공하는 REST 컨트롤러입니다.</p>
 *
 * <p>이 API는 사용자 플레이리스트의 조회, 저장 및 아티스트/트랙 검색 기능을 제공합니다.</p>
 */
@RestController
@RequestMapping("/api/")
@Tag(name = "Playlist 관련 API", description = "Playlist 노래 저장 및 제공")
public class PlaylistApi {

  private final PlaylistService playlistService;
  private final UsersRepository usersRepository;

  /**
   * <p>PlaylistApi의 생성자입니다.</p>
   *
   * @param playlistService 플레이리스트 관련 비즈니스 로직을 처리하는 서비스 클래스
   * @param usersRepository 사용자 관련 데이터베이스 접근 레포지토리
   */
  public PlaylistApi(PlaylistService playlistService, UsersRepository usersRepository) {
    this.playlistService = playlistService;
    this.usersRepository = usersRepository;
  }

  /**
   * <p>특정 사용자의 플레이리스트 데이터를 조회합니다.</p>
   *
   * @param userId 사용자 ID
   * @return 플레이리스트 데이터를 포함한 리스트
   * @throws NotFoundUserException 사용자를 찾을 수 없을 때 발생합니다.
   */
  @Transactional
  @GetMapping("/user/{userId}/playlist")
  @ApiResponse(responseCode = "404", description = "Not Found User")
  @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
      array = @ArraySchema(schema = @Schema(implementation = PlaylistDetail.class))))
  @Operation(summary = "플레이리스트 조회", description = "특정 유저의 플레이리스트 조회")
  public ResponseEntity<List<PlaylistDetail>> dataGive(
      @Parameter(description = "유저 id")
      @PathVariable Long userId) {
    return ResponseEntity.ok(playlistService.open(userId));
  }

  /**
   * <p>팔로우한 사용자의 플레이리스트 데이터를 조회합니다.</p>
   *
   * @param userId 사용자 ID
   * @return 팔로우한 사용자의 플레이리스트 데이터를 포함한 리스트
   * @throws NotFoundUserException 사용자를 찾을 수 없을 때 발생합니다.
   */
  @Transactional
  @GetMapping("/FollowerPlaylist/user/{userId}")
  @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
      array = @ArraySchema(schema = @Schema(implementation = FollowerPlaylistDetail.class))))
  @Operation(summary = "플레이리스트 요약 정보", description = "팔로우한 유저들의 플레이리스트 요약 정보 제공", deprecated = true)
  public ResponseEntity<List<FollowerPlaylistDetail>> followerDataGive(
      @Parameter(description = "유저 id")
      @PathVariable Long userId) {
    return ResponseEntity.ok(playlistService.followDataOpen(userId));
  }

  @GetMapping("/playlist/following")
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
      @Parameter(description = "플레이리스트에 담을 데이터")
      @RequestBody PlaylistDto playlist,
      @AuthenticationPrincipal Long userId) {
    return ResponseEntity.ok(playlistService.save(userId, playlist));
  }

  /**
   * <p>아티스트 이름으로 트랙을 검색합니다.</p>
   *
   * @param artistName 검색할 아티스트 이름
   * @return 해당 아티스트의 트랙 데이터를 포함한 리스트
   * @throws NotFoundArtistException 아티스트 이름이 일치하지 않을 때 발생합니다.
   */
  @GetMapping("/playlist/artist/{artistName}")
  @ApiResponse(responseCode = "400", description = "Not Match Artist Name")
  @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
      array = @ArraySchema(schema = @Schema(implementation = TrackDetail.class))))
  @Operation(summary = "아티스트 검색", description = "아티스트 이름을 통한 노래정보 제공")
  public ResponseEntity<List<TrackDetail>> artistSearch(
      @Parameter(description = "아티스트 이름")
      @PathVariable String artistName) {
    return ResponseEntity.ok(playlistService.artistSearch(artistName));
  }

  /**
   * <p>트랙 제목으로 트랙을 검색합니다.</p>
   *
   * @param trackName 검색할 트랙 제목
   * @return 해당 트랙 데이터를 포함한 리스트
   * @throws NotFoundTrackException 트랙 제목이 일치하지 않을 때 발생합니다.
   */
  @GetMapping("/playlist/track/{trackName}")
  @Transactional
  @ApiResponse(responseCode = "404", description = "Not Match Track Title")
  @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
      array = @ArraySchema(schema = @Schema(implementation = TrackDetail.class))))
  @Operation(summary = "노래 검색", description = "노래 이름 검색을 통한 노래정보 제공")
  public ResponseEntity<List<TrackDetail>> trackSearch(
      @Parameter(description = "노래 이름")
      @PathVariable String trackName) {
    return ResponseEntity.ok(playlistService.trackSearch(trackName));
  }
}
