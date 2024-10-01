package org.cosmic.backend.domain.favoriteArtist.apis;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.cosmic.backend.domain.favoriteArtist.applications.FavoriteArtistService;
import org.cosmic.backend.domain.favoriteArtist.dtos.*;
import org.cosmic.backend.domain.playList.exceptions.NotFoundArtistException;
import org.cosmic.backend.domain.playList.exceptions.NotFoundTrackException;
import org.cosmic.backend.domain.playList.exceptions.NotFoundUserException;
import org.cosmic.backend.domain.post.exceptions.NotFoundAlbumException;
import org.cosmic.backend.globals.annotations.ApiCommonResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p> FavoriteApi는 사용자의 즐겨찾기 아티스트와 관련된 REST API를 제공합니다. </p>
 *
 * <p> 이 API는 사용자의 즐겨찾기 아티스트 정보를 조회하고, 아티스트, 앨범, 트랙을 검색하며,
 * 사용자의 즐겨찾기 정보를 저장하는 기능을 제공합니다. </p>
 *
 */
@RestController
@RequestMapping("/api/")
@ApiCommonResponses
@Tag(name = "Favorite아티스트 관련 API", description = "Favorite아티스트 정보 제공 및 저장")
public class FavoriteApi {

    private final FavoriteArtistService favoriteartistService;

    /**
     * FavoriteApi의 생성자.
     *
     * @param favoriteartistService FavoriteArtistService의 인스턴스
     */
    public FavoriteApi(FavoriteArtistService favoriteartistService) {
        this.favoriteartistService = favoriteartistService;
    }

    /**
     * <p>사용자가 즐겨찾는 아티스트 정보를 반환합니다.</p>
     *
     * @param userId 사용자의 ID
     * @return 사용자가 즐겨찾는 아티스트 정보
     * @throws NotFoundUserException 사용자를 찾을 수 없는 경우 발생합니다.
     */
    @GetMapping("/user/{userId}/favoriteArtist")
    @ApiResponse(responseCode = "404", description = "Not Found User")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
            array = @ArraySchema(schema = @Schema(implementation = FavoriteArtistDetail.class))))
    @Operation(summary = "favorite아티스트 조회",description = "특정 유저의 favorite아티스트 조회")
    public ResponseEntity<FavoriteArtistDetail> favoriteArtistGiveData(
            @Parameter(description = "유저 id")
            @PathVariable Long userId) {
        return ResponseEntity.ok(favoriteartistService.favoriteArtistGiveData(userId));
    }

    /**
     * <p>주어진 아티스트 이름으로 관련된 앨범 및 트랙 데이터를 검색합니다.</p>
     *
     * @param artistName 아티스트의 이름
     * @return 해당 아티스트의 앨범 및 트랙 데이터 리스트
     * @throws NotFoundArtistException 아티스트를 찾을 수 없는 경우 발생합니다.
     */
    @GetMapping("/favoriteArtist/artist/{artistName}")
    @ApiResponse(responseCode = "404", description = "Not Found Artist")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
            array = @ArraySchema(schema = @Schema(implementation = ArtistDetail.class))))
    @Operation(summary = "아티스트 검색",description = "아티스트 이름으로 아티스트 정보 제공")
    public ResponseEntity<List<ArtistDetail>> artistSearchData(
            @Parameter(description = "아티스트 이름")
            @PathVariable String artistName) {
        return ResponseEntity.ok(favoriteartistService.artistSearchData(artistName));
    }

    /**
     * <p>주어진 앨범 이름과 아티스트 ID로 관련된 트랙 데이터를 검색합니다.</p>
     *
     * @param artistId 아티스트의 ID
     * @param albumName 앨범의 이름
     * @return 해당 앨범의 트랙 데이터 리스트
     * @throws NotFoundAlbumException 앨범을 찾을 수 없는 경우 발생합니다.
     */
    @GetMapping("/favoriteArtist/artist/{artistId}/album/{albumName}")
    @ApiResponse(responseCode = "404", description = "Not Found Album")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
            array = @ArraySchema(schema = @Schema(implementation = ArtistDetail.class))))
    @Operation(summary = "앨범 검색",description = "앨범으로 앨범 정보 제공")
    public ResponseEntity<List<AlbumDetail>> albumSearchData(
            @Parameter(description = "아티스트 id")
            @PathVariable Long artistId,
            @Parameter(description = "앨범 이름")
            @PathVariable String albumName) {
        return ResponseEntity.ok(favoriteartistService.albumSearchData(artistId, albumName));
    }

    /**
     * <p>주어진 앨범 ID와 트랙 이름으로 트랙 데이터를 검색합니다.</p>
     *
     * @param albumId 앨범의 ID
     * @param trackName 트랙의 이름
     * @return 해당 트랙의 데이터
     * @throws NotFoundTrackException 트랙을 찾을 수 없는 경우 발생합니다.
     */
    @GetMapping("/favoriteArtist/album/{albumId}/track/{trackName}")
    @ApiResponse(responseCode = "404", description = "Not Found Track")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
            array = @ArraySchema(schema = @Schema(implementation = ArtistDetail.class))))
    @Operation(summary = "노래 검색",description = "노래로 노래 정보 제공")
    public ResponseEntity<TrackDetail> trackSearchData(
            @Parameter(description = "앨범 id")
            @PathVariable Long albumId,
            @Parameter(description = "노래 이름")
            @PathVariable String trackName) {
        return ResponseEntity.ok(favoriteartistService.trackSearchData(albumId, trackName));
    }

    /**
     * <p>사용자의 즐겨찾기 아티스트 정보를 저장합니다.</p>
     *
     * @param favoriteartist 사용자의 즐겨찾기 아티스트 정보를 포함하는 요청 객체
     * @param userId 사용자의 ID
     * @return 저장된 즐겨찾기 아티스트 정보
     * @throws NotFoundUserException 사용자를 찾을 수 없는 경우 발생합니다.
     * @throws NotFoundTrackException 트랙을 찾을 수 없는 경우 발생합니다.
     * @throws NotFoundAlbumException 앨범을 찾을 수 없는 경우 발생합니다.
     * @throws NotFoundArtistException 아티스트를 찾을 수 없는 경우 발생합니다.
     */
    @PostMapping("/favoriteArtist")
    @ApiResponse(responseCode = "404", description = "Not Found User Or Track Or Album Or Artist")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
            array = @ArraySchema(schema = @Schema(implementation = ArtistDetail.class))))
    @Operation(summary = "favorite아티스트 저장",description = "특정 유저의 Favorite아티스트 정보 저장")
    public ResponseEntity<FavoriteArtistDetail> favoriteArtistSaveData(
            @Parameter(description = "favorite아티스트 등록 정보")
            @RequestBody FavoriteRequest favoriteartist,
            @AuthenticationPrincipal Long userId) {
        return ResponseEntity.ok(favoriteartistService.favoriteArtistSaveData(favoriteartist, userId));
    }
}
