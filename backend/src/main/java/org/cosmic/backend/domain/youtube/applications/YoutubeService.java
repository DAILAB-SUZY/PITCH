package org.cosmic.backend.domain.youtube.applications;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import org.cosmic.backend.domain.playList.repositorys.PlaylistRepository;
import org.cosmic.backend.domain.playList.repositorys.PlaylistTrackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class YoutubeService {

    //TODO 자동이 아닌 수동방식 고려
    @Autowired
    private PlaylistRepository playlistRepository;
    @Autowired
    private PlaylistTrackRepository playlistTrackRepository;
    @Autowired
    private YouTube youtube;
    private final RestTemplate restTemplate;

    public YoutubeService() {
        this.restTemplate = new RestTemplate();
    }
    private YouTube createYouTubeClient(OAuth2AuthenticationToken authentication) {
        String accessToken = authentication.getPrincipal().getAttribute("access_token");

        Credential credential = new GoogleCredential().setAccessToken(accessToken);

        return new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), credential)
                .setApplicationName("youtube-playlist-creator")
                .build();
    }
    public Map<String, Object> getAccessToken(String authorizationCode) {
        // 1.1. HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // 1.2. 요청 바디에 필요한 파라미터 설정
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("code", authorizationCode);
        body.add("client_id", System.getenv("YOUTUBECLIENT_ID"));  // 환경 변수에서 클라이언트 ID 가져오기
        body.add("client_secret", System.getenv("YOUTUBECLIENT_SECRET"));  // 환경 변수에서 클라이언트 비밀 가져오기
        body.add("redirect_uri", "http://localhost:8080/oauth2/callback/google");
        body.add("grant_type", "authorization_code");

        // 1.3. HTTP 요청 생성
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        // 1.4. Google의 OAuth2 토큰 엔드포인트 호출
        String tokenUrl = "https://oauth2.googleapis.com/token";
        ResponseEntity<Map> response = restTemplate.postForEntity(tokenUrl, request, Map.class);

        // 1.5. 응답 바디에서 Access Token 반환
        return response.getBody();
    }
/*
    public String createPlaylist(Long userId, OAuth2AuthenticationToken authentication) throws IOException {
        YouTube youtube = createYouTubeClient(authentication);

        PlaylistSnippet snippet = new PlaylistSnippet();

        List<Playlist_Track> tracks= playlistTrackRepository.findByPlaylist_PlaylistId(
            playlistRepository.findByUser_UserId(userId).get().getPlaylistId()).get();

        //해당 사용자가 가지고 있는 플레이리스트에 노래들을 가져옴.
        snippet.setTitle(title);

        PlaylistStatus status = new PlaylistStatus();
        status.setPrivacyStatus("public");

        Playlist playlist = new Playlist();
        playlist.setSnippet(snippet);
        playlist.setStatus(status);

        YouTube.Playlists.Insert request = youtube.playlists()
                .insert("snippet,status", playlist);

        Playlist response = request.execute();
        return response.getId();  // 생성된 플레이리스트 ID 반환
    }*/
}
