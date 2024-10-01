package org.cosmic.backend.domain.search.applications;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.cosmic.backend.domain.playList.domains.Album;
import org.cosmic.backend.domain.playList.domains.Artist;
import org.cosmic.backend.domain.playList.domains.Track;
import org.cosmic.backend.domain.playList.dtos.Image;
import org.cosmic.backend.domain.playList.repositorys.*;
import org.cosmic.backend.domain.search.dtos.SpotifySearchAlbumResponse;
import org.cosmic.backend.domain.search.dtos.SpotifySearchArtistResponse;
import org.cosmic.backend.domain.search.dtos.SpotifySearchTrackResponse;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class SearchService {
    private final TrackRepository trackRepository;
    private final ArtistRepository artistRepository;
    private final AlbumRepository albumRepository;

    public SearchService(TrackRepository trackRepository, ArtistRepository artistRepository, AlbumRepository albumRepository) {

        this.trackRepository = trackRepository;
        this.artistRepository = artistRepository;
        this.albumRepository = albumRepository;
    }

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

    public String searchArtist(String accessToken, String q) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        List<SpotifySearchArtistResponse> spotifySearchArtistResponses = new ArrayList<>();

        String data=search(accessToken,q);
        JsonNode rootNode = mapper.readTree(data);
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
        Artist artist=null;
        for(int i=0;i<spotifySearchArtistResponses.size();i++)
        {
            artist=artistRepository.save(Artist.builder()
                    .artistName(spotifySearchArtistResponses.get(i).getName())
                    .artistCover(String.valueOf(spotifySearchArtistResponses.get(i).getImages().get(0).getUrl()))
                    .build()
            );
        }

        /*
        * unique key로 spotify artistid를 만듦.
        * */
        return data;
    }

    public String searchTrack(String accessToken, String q) throws JsonProcessingException { // q는 검색어
        ObjectMapper mapper = new ObjectMapper();
        List<SpotifySearchArtistResponse> spotifySearchArtistResponses = new ArrayList<>();
        List<SpotifySearchTrackResponse> spotifySearchTrackResponses = new ArrayList<>();

        String data=search(accessToken,q);
        JsonNode rootNode = mapper.readTree(data);
        JsonNode trackitemsNode = rootNode.path("tracks").path("items");
        Album album=null;
        Track track=null;
        Artist artist=null;
        for(int i=0;i<trackitemsNode.size();i++)
        {
            SpotifySearchTrackResponse spotifySearchTrackResponse = new SpotifySearchTrackResponse();
            JsonNode item = trackitemsNode.get(i);
            spotifySearchTrackResponse.setTrackId(item.path("id").asText());
            spotifySearchTrackResponse.setTrackName(item.path("name").asText());

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

        for(int i=0;i<spotifySearchTrackResponses.size();i++)
        {
            for(int j=0;j<spotifySearchTrackResponses.get(i).getAlbums().getArtists().size();j++)
            {
                artist=artistRepository.save(Artist.builder()
                        .artistName(spotifySearchTrackResponses.get(i).getAlbums().getArtists().get(j).getName())
                        .artistCover(null)
                        //.artistCover(spotifySearchTrackResponses.get(i).getAlbums().getArtists().get(j).getImages().get(0).getUrl())
                        .build()
                );
            }
            // 문자열을 LocalDate로 변환
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate localDate = LocalDate.parse(spotifySearchTrackResponses.get(i).getAlbums().getRelease_date(), formatter);

            // LocalDate를 Instant로 변환 (UTC 기준)
            Instant releaseDateInstant = localDate.atStartOfDay(ZoneId.of("UTC")).toInstant();

            album=albumRepository.save(Album.builder()
                    .albumCover(spotifySearchTrackResponses.get(i).getAlbums().getImages().get(0).getUrl())
                    .title(spotifySearchTrackResponses.get(i).getAlbums().getName())
                    .createdDate(releaseDateInstant)
                    .artist(artist)
                    .build()
            );

            track=trackRepository.save(Track.builder()
                    .album(album)
                    .title(spotifySearchTrackResponses.get(i).getTrackName())
                    .artist(artist)
                    .trackCover(spotifySearchTrackResponses.get(i).getAlbums().getImages().get(0).getUrl())
                    .build()
            );
        }

        System.out.println(spotifySearchTrackResponses);
        return spotifySearchTrackResponses.toString();
    }

    public String searchAlbum(String accessToken, String q) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        List<SpotifySearchArtistResponse> spotifySearchArtistResponses = new ArrayList<>();
        List<SpotifySearchTrackResponse> spotifySearchTrackResponses = new ArrayList<>();

        String data=search(accessToken,q);
        JsonNode rootNode = mapper.readTree(data);
        List<SpotifySearchAlbumResponse> spotifySearchAlbumResponses = new ArrayList<>();
        Artist artist=null;
        Album album=null;
        try {
            JsonNode albumitemsNode = rootNode.path("albums").path("items");

            for (int i = 0; i < albumitemsNode.size(); i++) {
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
                item = albumitemsNode.get(i).path("artists");
                for (int j = 0; j < item.size(); j++) {
                    SpotifySearchArtistResponse spotifySearchArtistResponse = new SpotifySearchArtistResponse();
                    JsonNode artistsNode = item.get(j);
                    spotifySearchArtistResponse.setArtistId(artistsNode.path("id").asText());
                    spotifySearchArtistResponse.setName(artistsNode.path("name").asText());
                    spotifySearchArtistResponse.setImages(null);
                    spotifySearchArtistResponses.add(spotifySearchArtistResponse);
                }
                spotifySearchAlbumResponse.setArtists(spotifySearchArtistResponses);
                spotifySearchAlbumResponses.add(spotifySearchAlbumResponse);
            }

            for(int i=0;i<spotifySearchAlbumResponses.size();i++)
            {
                for(int j=0;j<spotifySearchAlbumResponses.get(i).getArtists().size();j++)
                {
                    artist=artistRepository.save(Artist.builder()
                        .artistName(spotifySearchAlbumResponses.get(i).getArtists().get(j).getName())
                        .artistCover(null)
                        //.artistCover(spotifySearchAlbumResponses.get(i).getArtists().get(i).getImages().get(0).getUrl())
                        .build());
                }



                // 문자열을 LocalDate로 변환
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate localDate = LocalDate.parse(spotifySearchAlbumResponses.get(i).getRelease_date(), formatter);

                // LocalDate를 Instant로 변환 (UTC 기준)
                Instant releaseDateInstant = localDate.atStartOfDay(ZoneId.of("UTC")).toInstant();

                album=albumRepository.save(Album.builder()
                        .albumCover(spotifySearchAlbumResponses.get(i).getImages().get(0).getUrl())
                        .title(spotifySearchAlbumResponses.get(i).getName())
                        .createdDate(releaseDateInstant)
                        .artist(artist)
                        .build()
                );
            }

        }
        catch (Exception e) {

        }
        return data; // 예외 발생 시 null 반환
    }


}
