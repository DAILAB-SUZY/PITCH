package org.cosmic.backend.domain.search.applications;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SearchApplication {
    public String search(String accessToken,String q){ //q는 검색어
        RestTemplate rest = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);;
        headers.add("Host", "api.spotify.com");
        headers.add("Content-type", "application/json");
        String body = "";

        HttpEntity<String> requestEntity = new HttpEntity<String>(body, headers);
        ResponseEntity<String> responseEntity = rest.exchange("https://api.spotify.com/v1/search?type=track&q=" + q, HttpMethod.GET, requestEntity, String.class);
        HttpStatus httpStatus = (HttpStatus) responseEntity.getStatusCode();
        int status = httpStatus.value(); //상태 코드가 들어갈 status 변수
        String response = responseEntity.getBody();

        //검색 후 데이터 추출 후 DB 저장
        return response;
    }
}
