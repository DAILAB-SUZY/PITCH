package org.cosmic.backend.domain.search.applications;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class SearchService {

    public HttpHeaders setting(String accessToken,HttpHeaders headers) {
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Host", "api.spotify.com");
        headers.add("Content-type", "application/json");
        return headers;
    }

    public String search(String accessToken, String q)
    {
        RestTemplate rest = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers = setting(accessToken,headers);

        String body = "";

        // 여러 타입을 검색하기 위해 type 파라미터에 track, artist, album을 포함
        String searchUrl = "https://api.spotify.com/v1/search?type=track,artist,album&q=" + q;

        HttpEntity<String> requestEntity = new HttpEntity<String>(body, headers);
        // 요청 시도 및 응답 처리
        ResponseEntity<String> responseEntity = rest.exchange(searchUrl, HttpMethod.GET, requestEntity, String.class);
        return responseEntity.getBody();
    }



    public String searchSpotifyTrack(String accessToken,String trackId)
    {
        RestTemplate rest = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers = setting(accessToken,headers);

        String body = "";
        String searchUrl = "https://api.spotify.com/v1/tracks/" + trackId;
        HttpEntity<String> requestEntity = new HttpEntity<String>(body, headers);
        ResponseEntity<String> responseEntity = rest.exchange(searchUrl, HttpMethod.GET, requestEntity, String.class);
        return responseEntity.getBody();
    }
    public String searchSpotifyArtist(String accessToken,String artistId)
    {
        RestTemplate rest = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers = setting(accessToken,headers);

        String body = "";
        String searchUrl = "https://api.spotify.com/v1/artists/" + artistId;
        HttpEntity<String> requestEntity = new HttpEntity<String>(body, headers);
        ResponseEntity<String> responseEntity = rest.exchange(searchUrl, HttpMethod.GET, requestEntity, String.class);
        return responseEntity.getBody();
    }
    public String searchSpotifyAlbum(String accessToken,String albumId)
    {
        RestTemplate rest = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers = setting(accessToken,headers);

        String body = "";
        String searchUrl = "https://api.spotify.com/v1/albums/" + albumId;
        HttpEntity<String> requestEntity = new HttpEntity<String>(body, headers);
        ResponseEntity<String> responseEntity = rest.exchange(searchUrl, HttpMethod.GET, requestEntity, String.class);
        return responseEntity.getBody();
    }

}
