package org.cosmic.backend.domain.search.applications;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.cosmic.backend.domain.playList.dtos.Image;
import org.cosmic.backend.domain.search.dtos.SpotifySearchAlbumResponse;
import org.cosmic.backend.domain.search.dtos.SpotifySearchArtistResponse;
import org.cosmic.backend.domain.search.dtos.SpotifySearchTrackResponse;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class SearchApplication {

    public HttpHeaders setting(String accessToken,HttpHeaders headers) {
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Host", "api.spotify.com");
        headers.add("Content-type", "application/json");
        return headers;
    }

    public String searchTrack(String accessToken, String q) { // q는 검색어
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

        return extrackTrackData(response);
    }

    public String extrackTrackData(String data)
    {
        ObjectMapper mapper = new ObjectMapper();
        List<SpotifySearchAlbumResponse> spotifySearchAlbumResponses = new ArrayList<>();
        List<SpotifySearchArtistResponse> spotifySearchArtistResponses = new ArrayList<>();
        List<SpotifySearchTrackResponse> spotifySearchTrackResponses = new ArrayList<>();

        try {
            //노래로 앨범을 검색하는 상황
            JsonNode rootNode = mapper.readTree(data);
            JsonNode albumitemsNode = rootNode.path("albums").path("items");

            for(int i=0;i<albumitemsNode.size();i++)
            {
                SpotifySearchAlbumResponse spotifySearchAlbumResponse = new SpotifySearchAlbumResponse();

                JsonNode item = albumitemsNode.get(i);
                spotifySearchAlbumResponse.setRelease_date(item.path("release_date").asText());
                spotifySearchAlbumResponse.setAlbumId(item.path("id").asText());
                spotifySearchAlbumResponse.setName(item.path("name").asText());

                List<Image> images = new ArrayList<>();
                JsonNode imagesNode = albumitemsNode.get(i).path("images");
                for (JsonNode imageNode : imagesNode) {
                    Image image = new Image(
                            imageNode.path("height").asInt(),
                            imageNode.path("url").asText(),
                            imageNode.path("width").asInt()
                    );
                    images.add(image);
                }

                spotifySearchAlbumResponse.setImages(images);
                item=albumitemsNode.get(i).path("artists");
                for(int j=0;j<item.size();j++)
                {
                    SpotifySearchArtistResponse spotifySearchArtistResponse = new SpotifySearchArtistResponse();
                    JsonNode artistsNode = item.get(j);
                    spotifySearchArtistResponse.setArtistId(artistsNode.path("id").asText());
                    spotifySearchArtistResponse.setName(artistsNode.path("name").asText());
                    spotifySearchArtistResponse.setImages(null);
                    spotifySearchArtistResponses.add(spotifySearchArtistResponse);
                }
                spotifySearchAlbumResponse.setArtists(spotifySearchArtistResponses);
                System.out.println("hellowww"+spotifySearchAlbumResponse);
                spotifySearchAlbumResponses.add(spotifySearchAlbumResponse);
                spotifySearchArtistResponses.clear();
            }
            //노래로 아티스트를 검색하는 상황

            spotifySearchArtistResponses.clear();
            JsonNode artistitemsNode = rootNode.path("artists").path("items");

           for(int i=0;i<artistitemsNode.size();i++)
            {
                SpotifySearchArtistResponse spotifySearchArtistResponse = new SpotifySearchArtistResponse();
                JsonNode item = artistitemsNode.get(i);
                spotifySearchArtistResponse.setArtistId(item.path("id").asText());
                spotifySearchArtistResponse.setName(item.path("name").asText());

                List<Image> images = new ArrayList<>();
                JsonNode imagesNode = artistitemsNode.get(i).path("images");
                for (JsonNode imageNode : imagesNode) {
                    Image image = new Image(
                            imageNode.path("height").asInt(),
                            imageNode.path("url").asText(),
                            imageNode.path("width").asInt()
                    );
                    images.add(image);
                }
                spotifySearchArtistResponse.setImages(images);
                spotifySearchArtistResponses.add(spotifySearchArtistResponse);
            }
           System.out.println("*******2"+spotifySearchArtistResponses);
           spotifySearchAlbumResponses.clear();
            spotifySearchArtistResponses.clear();
            //q로 노래를 검색하는 상황
            JsonNode trackitemsNode = rootNode.path("tracks").path("items");

            for(int i=0;i<trackitemsNode.size();i++)
            {
                SpotifySearchTrackResponse spotifySearchTrackResponse = new SpotifySearchTrackResponse();
                JsonNode item = trackitemsNode.get(i);
                spotifySearchTrackResponse.setTrackId(item.path("id").asText());
                spotifySearchTrackResponse.setTrackName(item.path("name").asText());

                //노래

                SpotifySearchAlbumResponse spotifySearchAlbumResponse = new SpotifySearchAlbumResponse();
                item = trackitemsNode.get(i).path("album");
                spotifySearchAlbumResponse.setName(item.path("name").asText());
                spotifySearchAlbumResponse.setRelease_date(item.path("release_date").asText());
                spotifySearchAlbumResponse.setAlbumId(item.path("id").asText());

                List<Image> images = new ArrayList<>();
                JsonNode imagesNode = item.path("images");
                for (JsonNode imageNode : imagesNode) {
                    Image image = new Image(
                            imageNode.path("height").asInt(),
                            imageNode.path("url").asText(),
                            imageNode.path("width").asInt()
                    );
                    images.add(image);
                }
                spotifySearchAlbumResponse.setImages(images);
                //앨범

                item = item.path("artists");

                spotifySearchArtistResponses.clear();
                for(int j=0;j<item.size();j++)
                {
                    SpotifySearchArtistResponse spotifySearchArtistResponse = new SpotifySearchArtistResponse();
                    JsonNode artistsNode = item.get(j);
                    spotifySearchArtistResponse.setArtistId(artistsNode.path("id").asText());
                    spotifySearchArtistResponse.setName(artistsNode.path("name").asText());
                    spotifySearchArtistResponse.setImages(null);
                    spotifySearchArtistResponses.add(spotifySearchArtistResponse);
                }
                spotifySearchAlbumResponse.setArtists(spotifySearchArtistResponses);

                spotifySearchTrackResponse.setAlbums(spotifySearchAlbumResponse);
                spotifySearchTrackResponses.add(spotifySearchTrackResponse);
                //아티스트
            }
            System.out.println("****3"+spotifySearchTrackResponses);
        }
        catch (Exception e) {

        }
        return data; // 예외 발생 시 null 반환
    }


}
