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
import org.cosmic.backend.domain.search.applications.SearchAlbumService;
import org.cosmic.backend.domain.search.applications.SearchArtistService;
import org.cosmic.backend.domain.search.dtos.SpotifySearchAlbumResponse;
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
public class SpotifySearchAlbumApi {

  CreateSpotifyToken createSpotifyToken = new CreateSpotifyToken();
  @Autowired
  private SearchAlbumService searchAlbumService;
  @Autowired
  private SearchArtistService searchArtistService;

  // 아티스트 또는 앨범 이름으로 앨범 정보 찾기
  @GetMapping("/searchSpotify/album/{name}")
  @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
      array = @ArraySchema(schema = @Schema(implementation = SpotifySearchAlbumResponse.class))))
  @Operation(summary = "특정 앨범 조회")
  public ResponseEntity<List<SpotifySearchAlbumResponse>> searchAlbum(
      @Parameter(description = "앨범 이름")
      @PathVariable String name) throws JsonProcessingException { //q는 검색어
    return ResponseEntity.ok(
        searchAlbumService.searchAlbum(createSpotifyToken.accesstoken(), name));
  }

  //id로 앨범 정보찾기
  @GetMapping("/searchSpotify/albumId/{spotifyAlbumId}")
  @ApiResponse(responseCode = "200", content = {
      @Content(schema = @Schema(contentMediaType = MediaType.APPLICATION_JSON_VALUE
          , implementation = SpotifySearchAlbumResponse.class))})
  @Operation(summary = "특정 앨범 조회")
  public ResponseEntity<SpotifySearchAlbumResponse> searchAlbumId(
      @Parameter(description = "앨범 id")
      @PathVariable String spotifyAlbumId) throws JsonProcessingException { //q는 검색어
    return ResponseEntity.ok(searchAlbumService.searchAlbumId(createSpotifyToken.accesstoken(),
        spotifyAlbumId));
  }

  @GetMapping("/searchSpotify/artist/{spotifyArtistId}/album")
  @Operation(summary = "특정 아티스트의 앨범들 조회")
  @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
      array = @ArraySchema(schema = @Schema(implementation = SpotifySearchAlbumResponse.class))))
  public ResponseEntity<List<SpotifySearchAlbumResponse>> searchAlbumByArtistId(
      @Parameter(description = "아티스트 ID")
      @PathVariable String spotifyArtistId) throws JsonProcessingException { //q는 검색어
    return ResponseEntity.ok(
        searchArtistService.searchAlbumByArtistId(createSpotifyToken.accesstoken(),
            spotifyArtistId));
  }

  @GetMapping("/searchSpotify/artist/{spotifyArtistId}/album/{albumName}")
  @Operation(summary = "특정 아티스트의 앨범들 조회")
  @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
      array = @ArraySchema(schema = @Schema(implementation = SpotifySearchAlbumResponse.class))))
  public ResponseEntity<List<SpotifySearchAlbumResponse>> searchAlbumByArtistIdAndAlbumName(
      @Parameter(description = "아티스트 ID")
      @PathVariable String spotifyArtistId, @PathVariable String albumName)
      throws JsonProcessingException { //q는 검색어
    return ResponseEntity.ok(
        searchAlbumService.searchAlbumByArtistIdAndAlbumName(createSpotifyToken.accesstoken(),
            spotifyArtistId, albumName));
  }

}
