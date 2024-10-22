package org.cosmic.backend.domain.youtube.apis;

import org.cosmic.backend.domain.youtube.applications.YoutubeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class YoutubeApi {
    @Autowired
    YoutubeService youtubeService;

    @GetMapping("/oauth2/callback/google")
    public ResponseEntity<String> createPlaylist(@RequestParam("code") String code) {
        // 서비스에서 액세스 토큰을 받아옴
        return ResponseEntity.ok(youtubeService.getAccessToken(code));
    }

    @PostMapping("/api/createPlaylist")
    public ResponseEntity<String> createPlaylist(@AuthenticationPrincipal Long userId){//플레이리스트 설명과 제목도 넣을것인지.

        String playlistId = youtubeService.createPlaylists(userId);
        if (playlistId.equals("Failed to create playlist.")) {
            return ResponseEntity.status(500).body("Failed to create playlist.");
        }

        return ResponseEntity.ok("Playlist created successfully. ID: " + playlistId);
    }

}
