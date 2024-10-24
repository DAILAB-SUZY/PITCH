package org.cosmic.backend.domain.youtube.applications;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.cosmic.backend.domain.playList.domains.Playlist_Track;
import org.cosmic.backend.domain.playList.exceptions.NotFoundUserException;
import org.cosmic.backend.domain.playList.repositorys.PlaylistRepository;
import org.cosmic.backend.domain.playList.repositorys.PlaylistTrackRepository;
import org.cosmic.backend.domain.youtube.exceptions.*;
import org.cosmic.backend.globals.exceptions.UnAuthorizationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import se.michaelthelin.spotify.exceptions.detailed.BadRequestException;
import se.michaelthelin.spotify.exceptions.detailed.ForbiddenException;

import java.util.List;

@Service
public class YoutubeService {
    private static final String TOKEN_URL = "https://oauth2.googleapis.com/token";
    private final String clientId = System.getenv("YOUTUBE_CLIENT_ID");
    private final String clientSecret = System.getenv("YOUTUBE_CLIENT_SECRET");
    private final String redirectUri = "http://localhost:8080/oauth2/callback/google";
    private static final String YOUTUBE_PLAYLIST_URL = "https://www.googleapis.com/youtube/v3/playlists";
    private static final String YOUTUBE_SEARCH_URL = "https://www.googleapis.com/youtube/v3/search";
    private static final String YOUTUBE_PLAYLIST_ITEMS_URL = "https://www.googleapis.com/youtube/v3/playlistItems";
    @Autowired
    private PlaylistTrackRepository playlistTrackRepository;
    @Autowired
    private PlaylistRepository playlistRepository;
    private RestTemplate restTemplate = new RestTemplate();

    private String requestAccessToken(HttpEntity<MultiValueMap<String, String>> request)
    {
        try{
            // Google 서버로 POST 요청 전송
            ResponseEntity<String> response = restTemplate.exchange(
                    TOKEN_URL,
                    HttpMethod.POST,
                    request,
                    String.class
            );
            // 응답 본문에서 액세스 토큰 파싱
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            return jsonNode.get("access_token").asText();
        }
        //허가되지 않은 사용자일경우
        catch(JsonProcessingException e){
            throw new NotProcessApiException();
        }
    }

    // 액세스 토큰을 요청하는 메서드
    public String getAccessToken(String authorizationCode) {
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("code", authorizationCode);
        requestBody.add("client_id", clientId);
        requestBody.add("client_secret", clientSecret);
        requestBody.add("redirect_uri", redirectUri);
        requestBody.add("grant_type", "authorization_code");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(requestBody, headers);
        return requestAccessToken(request);
    }

    private String requestPlaylist(HttpEntity<String> request) {
        try{
            ResponseEntity<String> response = restTemplate.exchange(
                    YOUTUBE_PLAYLIST_URL + "?part=snippet",
                    HttpMethod.POST,
                    request,
                    String.class
            );

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            // 생성된 플레이리스트 ID 반환
            return jsonNode.get("id").asText();
        }
        catch(UnAuthorizationException e){
            throw new NotMatchKeyException();
        }
        catch(JsonProcessingException e){
            throw new NotProcessApiException();
        }
    }

    public String createPlaylist(String title, String description,String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        String requestBody = """
            {
              "snippet": {
                "title": "%s",
                "description": "%s",
                "privacyStatus": "public"
              }
            }
            """.formatted(title, description);
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
        return requestPlaylist(request);
    }

    private String requestVideo(HttpEntity<Void> request,String query) {
        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    YOUTUBE_SEARCH_URL + "?part=snippet&q=" + query + "&maxResults=1",
                    HttpMethod.GET,
                    request,
                    String.class
            );
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            return jsonNode.get("items").get(0).get("id").get("videoId").asText();
        }
        catch(JsonProcessingException e){
            throw new NotProcessApiException();
        }
    }

    public String searchVideo(String query,String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);
        return requestVideo(request,query);
    }

    // 플레이리스트에 비디오 추가
    public void addVideoToPlaylist(String playlistId, String videoId,String accessToken) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        String requestBody = """
            {
              "snippet": {
                "playlistId": "%s",
                "resourceId": {
                  "kind": "youtube#video",
                  "videoId": "%s"
                }
              }
            }
            """.formatted(playlistId, videoId);

        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        restTemplate.exchange(
                YOUTUBE_PLAYLIST_ITEMS_URL + "?part=snippet",
                HttpMethod.POST,
                request,
                String.class
        );

    }

    public String createPlaylists(Long userId,String title,String description,String accessToken) {
       if (accessToken==null) {
            throw new AuthorizationException();
        }
        String playlistId = createPlaylist(title,description,accessToken);
        List<Playlist_Track> playlistTracks= playlistTrackRepository.findByPlaylist_PlaylistId(
            playlistRepository.findByUser_UserId(userId).get().getPlaylistId()).get();
        for(int i=0; i<playlistTracks.size(); i++) {
            String query=playlistTracks.get(i).getTrack().getArtist().getArtistName()+" "+playlistTracks.get(i).getTrack().getTitle()+" official";
            String videoId = searchVideo(query,accessToken);
            if (videoId != null) {
                addVideoToPlaylist(playlistId, videoId,accessToken);
            }
        }
        return playlistId;
    }
}
