package org.cosmic.backend.domain.search.apis;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.cosmic.backend.domain.auth.applications.CreateSpotifyToken;
import org.cosmic.backend.domain.favoriteArtist.dtos.FavoriteArtistDetail;
import org.cosmic.backend.domain.search.applications.SearchAlbumService;
import org.cosmic.backend.domain.search.applications.SearchArtistService;
import org.cosmic.backend.domain.search.applications.SearchService;
import org.cosmic.backend.domain.search.applications.SearchTrackService;
import org.cosmic.backend.domain.search.dtos.SearchRequest;
import org.cosmic.backend.domain.search.dtos.SpotifySearchAlbumResponse;
import org.cosmic.backend.domain.search.dtos.SpotifySearchArtistResponse;
import org.cosmic.backend.domain.search.dtos.SpotifySearchTrackResponse;
import org.cosmic.backend.globals.annotations.ApiCommonResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/")
@ApiCommonResponses
@Tag(name = "스포티파이 검색 관련 API", description = "스포티파이를 통한 데이터 검색 제공")
public class SpotifySearchApi {
    @Autowired
    private SearchAlbumService searchAlbumService;
    @Autowired
    private SearchArtistService searchArtistService;
    @Autowired
    private SearchTrackService searchtrackService;

    CreateSpotifyToken createSpotifyToken=new CreateSpotifyToken();

    //아티스트, 노래 이름으로 앨범, 노래, 아티스트 모든 정보 주기.
    @GetMapping("/searchSpotify/track/{name}")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
            array = @ArraySchema(schema = @Schema(implementation = SpotifySearchTrackResponse.class))))
    @Operation(summary = "특정 노래 조회",description = "특정 노래 이름으로 유사한 이름의 노래 정보 조회")
    public ResponseEntity<List<SpotifySearchTrackResponse>> searchTrack(
        @Parameter(description = "노래 이름")
        @PathVariable String name) throws JsonProcessingException { //q는 검색어
        return ResponseEntity.ok(searchtrackService.searchTrack(createSpotifyToken.accesstoken(),name));
    }

    //아티스트만 검색하는 상황
    @GetMapping("/searchSpotify/artist/{name}")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
            array = @ArraySchema(schema = @Schema(implementation = SpotifySearchArtistResponse.class))))
    @Operation(summary = "특정 아티스트 조회",description = "특정 아티스트 이름으로 유사한 이름의 아티스트 정보 조회")
    public ResponseEntity<List<SpotifySearchArtistResponse>> searchArtist(
        @Parameter(description = "아티스트 이름")
        @PathVariable String name) throws JsonProcessingException { //q는 검색어
        return ResponseEntity.ok(searchArtistService.searchArtist(createSpotifyToken.accesstoken(),name));
    }

    // 아티스트 또는 앨범 이름으로 앨범 정보 찾기
    @GetMapping("/searchSpotify/album/{name}")
    @Operation(summary = "특정 앨범 조회")
    public ResponseEntity<List<SpotifySearchAlbumResponse>> searchAlbum(
        @Parameter(description = "앨범 이름")
        @PathVariable String name) throws JsonProcessingException { //q는 검색어
        return ResponseEntity.ok(searchAlbumService.searchAlbum(createSpotifyToken.accesstoken(),name));
    }
}
