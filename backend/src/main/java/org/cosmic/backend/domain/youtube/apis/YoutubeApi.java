package org.cosmic.backend.domain.youtube.apis;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.cosmic.backend.domain.search.dtos.SpotifySearchAlbumResponse;
import org.cosmic.backend.domain.search.dtos.SpotifySearchTrackResponse;
import org.cosmic.backend.domain.youtube.applications.YoutubeService;
import org.cosmic.backend.domain.youtube.dtos.PlaylistInforDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
public class YoutubeApi {
    @Autowired
    YoutubeService youtubeService;

    @ApiResponse(responseCode = "200", content = {@Content(schema=@Schema(contentMediaType = MediaType.APPLICATION_JSON_VALUE
            ,implementation= String.class))})
    @Operation(summary = "OAUTH로그인 후 AccessToken발급",description = "사용자가 OAUTH로그인을 완료하면 코드를 활용해 Token을 발급함")
    @GetMapping("/oauth2/callback/google")
    public ResponseEntity<String> createPlaylist(
            @Parameter(description = "유저 인증 코드")
            @RequestParam("code") String code) {
        return ResponseEntity.ok(youtubeService.getAccessToken(code));//인증이되어있는지안되어있느지
    }

    @ApiResponse(responseCode = "200", content = {@Content(schema=@Schema(contentMediaType = MediaType.APPLICATION_JSON_VALUE
            ,implementation= String.class))})
    @Operation(summary = "플레이리스트 공유",description = "Token을 활용한 특정 유저 유튜브에 플레이리스트 공유")
    @PostMapping("/api/createPlaylist")
    public ResponseEntity<String> createPlaylist(
            @Parameter(description = "플레이리스트 내용 및 accesstoken발급")
            @RequestBody PlaylistInforDetail playlistInforDetail,
            @AuthenticationPrincipal Long userId){
        String playlistId = youtubeService.createPlaylists(userId,playlistInforDetail.getTitle(),playlistInforDetail.getDescription(),playlistInforDetail.getYoutubeaccesstoken());
        return ResponseEntity.ok(playlistId);
    }

}
