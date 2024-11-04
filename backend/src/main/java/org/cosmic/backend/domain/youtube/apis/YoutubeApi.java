package org.cosmic.backend.domain.youtube.apis;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.net.URI;
import org.cosmic.backend.domain.youtube.applications.YoutubeService;
import org.cosmic.backend.domain.youtube.dtos.PlaylistInforDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class YoutubeApi {

  @Autowired
  YoutubeService youtubeService;

  @ApiResponse(responseCode = "200", content = {
      @Content(schema = @Schema(contentMediaType = MediaType.APPLICATION_JSON_VALUE
          , implementation = String.class))})
  @Operation(summary = "플레이리스트 공유", description = "Token을 활용한 특정 유저 유튜브에 플레이리스트 공유")
  @PostMapping("/api/createPlaylist")
  public ResponseEntity<String> createPlaylist(
      @Parameter(description = "플레이리스트 내용 및 accesstoken발급")
      @RequestBody PlaylistInforDetail playlistInforDetail,
      @AuthenticationPrincipal Long userId) {
    String playlistId = youtubeService.createPlaylists(userId,
        playlistInforDetail.getYoutubeaccesstoken());
    HttpHeaders headers = new HttpHeaders();
    headers.setLocation(
        URI.create("https://www.youtube.com/playlist?list=" + playlistId)); // 리디렉트할 URL 설정
    return new ResponseEntity<>(headers, HttpStatus.FOUND); // 302 리디렉트
  }
}
