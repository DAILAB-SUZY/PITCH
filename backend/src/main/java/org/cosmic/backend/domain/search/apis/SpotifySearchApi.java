package org.cosmic.backend.domain.search.apis;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.cosmic.backend.domain.auth.applications.CreateSpotifyToken;
import org.cosmic.backend.domain.search.applications.SearchAlbumService;
import org.cosmic.backend.domain.search.applications.SearchArtistService;
import org.cosmic.backend.domain.search.applications.SearchService;
import org.cosmic.backend.domain.search.applications.SearchTrackService;
import org.cosmic.backend.domain.search.dtos.SearchRequest;
import org.cosmic.backend.domain.search.dtos.SpotifySearchAlbumResponse;
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
    @GetMapping("/spotifyToken")
    public ResponseEntity<String> getToken(){ //q는 검색어
        return ResponseEntity.ok(createSpotifyToken.accesstoken());
    }
    //노래 검색

    //아티스트, 노래 이름으로 앨범, 노래, 아티스트 모든 정보 주기.
    @GetMapping("/searchSpotify/track/{name}")
    @Operation(summary = "특정 노래 조회")
    public ResponseEntity<String> searchTrack(@PathVariable String name) throws JsonProcessingException { //q는 검색어
        String accessToken=createSpotifyToken.accesstoken();
        return ResponseEntity.ok(searchtrackService.searchTrack(accessToken,name));
    }//노래+아티스트+앨범 ->

    //아티스트만 검색하는 상황
    @GetMapping("/searchSpotify/artist/{name}")
    @Operation(summary = "특정 아티스트 조회")
    public ResponseEntity<String> searchArtist(@PathVariable String name) throws JsonProcessingException { //q는 검색어
        String accessToken=createSpotifyToken.accesstoken();
        return ResponseEntity.ok(searchArtistService.searchArtist(accessToken,name));
    }//아티스트 정보만.

    // 아티스트 또는 앨범 이름으로 앨범 정보 찾기
    @GetMapping("/searchSpotify/album/{name}")
    @Operation(summary = "특정 앨범 조회")
    public ResponseEntity<List<SpotifySearchAlbumResponse>> searchAlbum(@PathVariable String name) throws JsonProcessingException { //q는 검색어
        String accessToken=createSpotifyToken.accesstoken();
        return ResponseEntity.ok(searchAlbumService.searchAlbum(accessToken,name));
    }//앨범 아티스트 정보를 주는
}
