package org.cosmic.backend.domain.youtube.apis;

import org.cosmic.backend.domain.youtube.applications.YoutubeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/auth2")
public class YoutubeController {
    private final YoutubeService youtubeService;
    public YoutubeController(YoutubeService youtubeService)
    {
        this.youtubeService = youtubeService;
    }

    @GetMapping("/callback/google")
    public String handleGoogleCallback(
            @RequestParam String code,
            @RequestParam(required = false) String state) {

        // Authorization Code 추출
        System.out.println("Authorization Code: " + code);

        // TODO: 받은 code를 서비스로 전달해 Access Token을 요청합니다.
        return "Authorization Code: " + code;
    }

    @GetMapping("/authorization/google")
    public String redirectToGoogle() {
        // Google OAuth2 로그인 페이지로 리디렉션
        return "Google OAuth2 로그인 페이지로 리디렉션하세요.";
    }
    @GetMapping("/api/token")
    public String getAccessToken(@RequestParam String code) {
        Map<String, Object> tokenResponse = youtubeService.getAccessToken(code);
        return "Access Token: " + tokenResponse.get("access_token");
    }
    @PostMapping("/playlist/create")
    public ResponseEntity<String> createPlaylist(
            @AuthenticationPrincipal Long userId, OAuth2AuthenticationToken authentication) throws IOException {
        System.out.println("*********hi");
        return ResponseEntity.ok("hi");//youtubeService.createPlaylist(userId,authentication)
    }


}
