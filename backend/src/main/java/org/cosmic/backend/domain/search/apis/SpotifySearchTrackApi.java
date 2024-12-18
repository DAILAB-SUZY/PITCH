package org.cosmic.backend.domain.search.apis;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.cosmic.backend.domain.auth.applications.CreateSpotifyToken;
import org.cosmic.backend.domain.search.applications.SearchArtistService;
import org.cosmic.backend.domain.search.applications.SearchTrackService;
import org.cosmic.backend.domain.search.dtos.ArtistTrackResponse;
import org.cosmic.backend.domain.search.dtos.SpotifySearchTrackResponse;
import org.cosmic.backend.globals.annotations.ApiCommonResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/")
@ApiCommonResponses
@Tag(name = "스포티파이 검색 관련 API", description = "스포티파이를 통한 데이터 검색 제공")
public class SpotifySearchTrackApi {

  CreateSpotifyToken createSpotifyToken = new CreateSpotifyToken();
  @Autowired
  private SearchArtistService searchArtistService;
  @Autowired
  private SearchTrackService searchTrackService;

  //아티스트, 노래 이름으로 앨범, 노래, 아티스트 모든 정보 주기.
  @GetMapping("/searchSpotify/track/{name}")
  @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
      array = @ArraySchema(schema = @Schema(implementation = SpotifySearchTrackResponse.class))))
  @Operation(summary = "특정 노래 조회", description = "특정 노래 이름으로 유사한 이름의 노래 정보 조회")
  public ResponseEntity<List<SpotifySearchTrackResponse>> searchTrack(
      @Parameter(description = "노래 이름")
      @PathVariable String name) throws JsonProcessingException { //q는 검색어
    return ResponseEntity.ok(
        searchTrackService.searchTrack(createSpotifyToken.accesstoken(), name));
  }

  @GetMapping("/searchSpotify/artist/{spotifyArtistId}/track")
  @Operation(summary = "특정 아티스트의 노래들 조회")
  @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
      array = @ArraySchema(schema = @Schema(implementation = SpotifySearchTrackResponse.class))))
  public ResponseEntity<List<SpotifySearchTrackResponse>> searchTrackByArtistId(
      @Parameter(description = "아티스트 Id")
      @PathVariable String spotifyArtistId) throws JsonProcessingException { //q는 검색어
    return ResponseEntity.ok(
        searchArtistService.searchTrackByArtistId(createSpotifyToken.accesstoken(),
            spotifyArtistId));
  }

  @GetMapping("/searchSpotify/artist/{spotifyArtistId}/track/{trackName}")
  @Operation(summary = "특정 아티스트의 노래들 조회")
  @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
      array = @ArraySchema(schema = @Schema(implementation = ArtistTrackResponse.class))))
  public ResponseEntity<List<ArtistTrackResponse>> searchTrackByArtistIdAndTrackName(
      @Parameter(description = "아티스트 Id")
      @PathVariable String spotifyArtistId, @PathVariable String trackName)
      throws JsonProcessingException { //q는 검색어
    return ResponseEntity.ok(
        searchTrackService.searchTrackByArtistIdAndTrackName(createSpotifyToken.accesstoken(),
            spotifyArtistId, trackName));
  }

}
