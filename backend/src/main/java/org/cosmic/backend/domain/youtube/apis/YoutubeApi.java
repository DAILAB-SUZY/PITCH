package org.cosmic.backend.domain.youtube.apis;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.net.URI;
import org.cosmic.backend.domain.youtube.applications.YoutubeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class YoutubeApi {

  private static final String serverOrigin = System.getenv("REDIRECTION_ORIGIN");
  @Autowired
  YoutubeService youtubeService;

  @ApiResponse(responseCode = "200", content = {
      @Content(schema = @Schema(contentMediaType = MediaType.APPLICATION_JSON_VALUE
          , implementation = String.class))})
  @Operation(summary = "OAUTH로그인 후 AccessToken발급", description = "사용자가 OAUTH로그인을 완료하면 코드를 활용해 Token을 발급함")
  @GetMapping("/oauth2/callback/google")
  public ResponseEntity<String> getToken(
      @Parameter(description = "유저 인증 코드")
      @RequestParam("code") String code,
      @Parameter(description = "유저 아이디")
      @RequestParam("state") Long userId) {
    HttpHeaders headers = new HttpHeaders();
    headers.setLocation(
        URI.create("https://%s/api/createPlaylist?googleToken=".formatted(serverOrigin)
            + youtubeService.getAccessToken(code) + "&userId=" + userId));
    return new ResponseEntity<>(headers, HttpStatus.FOUND);
  }

  @ApiResponse(responseCode = "200", content = {
      @Content(schema = @Schema(contentMediaType = MediaType.APPLICATION_JSON_VALUE
          , implementation = String.class))})
  @Operation(summary = "플레이리스트 공유", description = "Token을 활용한 특정 유저 유튜브에 플레이리스트 공유")
  @GetMapping("/api/createPlaylist")
  public ResponseEntity<String> createPlaylist(
      @Parameter(description = "구글 accesstoken")
      @RequestParam String googleToken,
      @Parameter(description = "유저 아이디")
      @RequestParam Long userId) {
    String playlistId = youtubeService.createPlaylists(userId, googleToken);
    HttpHeaders headers = new HttpHeaders();
    headers.setLocation(
        URI.create("https://www.youtube.com/playlist?list=" + playlistId));
    return new ResponseEntity<>(headers, HttpStatus.FOUND);
  }
}
