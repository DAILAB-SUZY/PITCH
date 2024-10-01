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

        // HttpStatusCode를 HttpStatus로 변환
        HttpStatus httpStatus = HttpStatus.valueOf(responseEntity.getStatusCode().value());
        String response = responseEntity.getBody();

        // 검색 후 데이터 추출 후 DB 저장 (현재는 단순히 응답 반환)
        return response;
    }

    public String searchArtistImg(String accessToken,String artistId)
    {
        RestTemplate rest = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers = setting(accessToken,headers);

        String body = "";
        String searchUrl = "https://api.spotify.com/v1/artists/" + artistId;
        HttpEntity<String> requestEntity = new HttpEntity<String>(body, headers);
        ResponseEntity<String> responseEntity = rest.exchange(searchUrl, HttpMethod.GET, requestEntity, String.class);

        HttpStatus httpStatus = HttpStatus.valueOf(responseEntity.getStatusCode().value());
        String response = responseEntity.getBody();

        return response;
    }
}
