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
import org.cosmic.backend.domain.search.applications.SearchAlbumService;
import org.cosmic.backend.domain.search.applications.SearchArtistService;
import org.cosmic.backend.domain.search.applications.SearchTrackService;
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
public class SpotifySearchArtistApi {
    @Autowired
    private SearchArtistService searchArtistService;

    CreateSpotifyToken createSpotifyToken=new CreateSpotifyToken();

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
}
