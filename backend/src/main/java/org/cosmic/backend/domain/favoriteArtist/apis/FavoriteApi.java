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
