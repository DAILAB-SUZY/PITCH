package org.cosmic.backend.domain.search.apis;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.cosmic.backend.domain.auth.applications.CreateSpotifyToken;
import org.cosmic.backend.domain.search.applications.SearchApplication;
import org.cosmic.backend.domain.search.dtos.SearchRequest;
import org.cosmic.backend.globals.annotations.ApiCommonResponses;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/")
@ApiCommonResponses
@Tag(name = "스포티파이 검색 관련 API", description = "스포티파이를 통한 데이터 검색 제공")
public class SpotifySearchApi {

    CreateSpotifyToken createSpotifyToken=new CreateSpotifyToken();
    SearchApplication searchApplication=new SearchApplication();

    @GetMapping("/spotifyToken")
    public ResponseEntity<String> getToken(){ //q는 검색어
        return ResponseEntity.ok(createSpotifyToken.accesstoken());
    }

    //노래 검색
    @GetMapping("/searchSpotify/track")
    @Operation(summary = "특정 노래 조회")
    public ResponseEntity<String> searchTrack(@RequestBody SearchRequest searchRequest) throws JsonProcessingException { //q는 검색어

        return ResponseEntity.ok(searchApplication.searchTrack(searchRequest.getAccessToken(),searchRequest.getQ()));
    }
}
