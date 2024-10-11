package org.cosmic.backend.domain.search.applications;

import jakarta.transaction.Transactional;
import java.util.List;
import org.cosmic.backend.domain.auth.applications.CreateSpotifyToken;
import org.cosmic.backend.domain.playList.repositorys.AlbumRepository;
import org.cosmic.backend.domain.playList.repositorys.ArtistRepository;
import org.cosmic.backend.domain.playList.repositorys.TrackRepository;
import org.cosmic.backend.domain.search.dtos.Album;
import org.cosmic.backend.domain.search.dtos.Artist;
import org.cosmic.backend.domain.search.dtos.Track;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class SearchService {

  @Autowired
  private AlbumRepository albumRepository;
  @Autowired
  private TrackRepository trackRepository;
  @Autowired
  private ArtistRepository artistRepository;

  public HttpHeaders setting(String accessToken, HttpHeaders headers) {
    headers.add("Authorization", "Bearer " + accessToken);
    headers.add("Host", "api.spotify.com");
    headers.add("Content-type", "application/json");
    headers.add("Accept-Language", "ko");
    return headers;
  }

  public String search(String accessToken, String q) {
    RestTemplate rest = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();
    headers = setting(accessToken, headers);

    String body = "";

    // 여러 타입을 검색하기 위해 type 파라미터에 track, artist, album을 포함
    String searchUrl = "https://api.spotify.com/v1/search?type=track,artist,album&q=" + q;

    HttpEntity<String> requestEntity = new HttpEntity<String>(body, headers);
    // 요청 시도 및 응답 처리
    ResponseEntity<String> responseEntity = rest.exchange(searchUrl, HttpMethod.GET, requestEntity,
        String.class);
    return responseEntity.getBody();
  }


  public String searchSpotifyTrack(String accessToken, String trackId) {
    RestTemplate rest = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();
    headers = setting(accessToken, headers);

    String body = "";
    String searchUrl = "https://api.spotify.com/v1/tracks/" + trackId;
    HttpEntity<String> requestEntity = new HttpEntity<String>(body, headers);
    ResponseEntity<String> responseEntity = rest.exchange(searchUrl, HttpMethod.GET, requestEntity,
        String.class);
    return responseEntity.getBody();
  }

  public String searchSpotifyArtist(String accessToken, String artistId) {
    RestTemplate rest = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();
    headers = setting(accessToken, headers);

    String body = "";
    String searchUrl = "https://api.spotify.com/v1/artists/" + artistId;
    HttpEntity<String> requestEntity = new HttpEntity<String>(body, headers);
    ResponseEntity<String> responseEntity = rest.exchange(searchUrl, HttpMethod.GET, requestEntity,
        String.class);
    return responseEntity.getBody();
  }

  public String searchSpotifyAlbum(String accessToken, String albumId) {
    RestTemplate rest = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();
    headers = setting(accessToken, headers);

    String body = "";
    String searchUrl = "https://api.spotify.com/v1/albums/" + albumId;
    HttpEntity<String> requestEntity = new HttpEntity<String>(body, headers);
    ResponseEntity<String> responseEntity = rest.exchange(searchUrl, HttpMethod.GET, requestEntity,
        String.class);
    return responseEntity.getBody();
  }

  private HttpEntity<String> getEntity() {
    CreateSpotifyToken createSpotifyToken = new CreateSpotifyToken();
    HttpHeaders headers = setting(createSpotifyToken.accesstoken(), new HttpHeaders());
    return new HttpEntity<>(headers);
  }


  public Album findAlbumBySpotifyId(String spotifyAlbumId) {
    String url = "https://api.spotify.com/v1/albums/" + spotifyAlbumId;
    RestTemplate rest = new RestTemplate();
    return rest.exchange(url, HttpMethod.GET, getEntity(), Album.class).getBody();
  }

  protected Artist findArtistBySpotifyId(String spotifyArtistId) {
    String url = "https://api.spotify.com/v1/artists/" + spotifyArtistId;
    RestTemplate rest = new RestTemplate();
    return rest.exchange(url, HttpMethod.GET, getEntity(), Artist.class).getBody();
  }

  public String searchSpotifyAlbumByArtistId(String accessToken, String artistId) {
    RestTemplate rest = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();
    headers = setting(accessToken, headers);

    String body = "";
    String searchUrl = "https://api.spotify.com/v1/artists/" + artistId + "/albums";
    HttpEntity<String> requestEntity = new HttpEntity<String>(body, headers);
    ResponseEntity<String> responseEntity = rest.exchange(searchUrl, HttpMethod.GET, requestEntity,
        String.class);
    return responseEntity.getBody();
  }

  public String searchSpotifyTrackByArtistId(String accessToken, List<String> albumIds) {
    RestTemplate rest = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();
    headers = setting(accessToken, headers);
    String body = "";
    String temp = "ids=";
    for (int i = 0; i < albumIds.size(); i++) {
      if (i == albumIds.size() - 1) {
        temp = temp + albumIds.get(i);
        break;
      }
      temp = temp + albumIds.get(i) + ",";
    }
    String searchUrl = "https://api.spotify.com/v1/albums?" + temp;
    System.out.println(searchUrl);
    HttpEntity<String> requestEntity = new HttpEntity<String>(body, headers);
    ResponseEntity<String> responseEntity = rest.exchange(searchUrl, HttpMethod.GET, requestEntity,
        String.class);
    return responseEntity.getBody();
  }

  public Track findTrackBySpotifyId(String spotifyTrackId) {
    String url = "https://api.spotify.com/v1/tracks/" + spotifyTrackId;
    RestTemplate rest = new RestTemplate();
    return rest.exchange(url, HttpMethod.GET, getEntity(), Track.class).getBody();
  }

  @Transactional
  public org.cosmic.backend.domain.playList.domains.Track findAndSaveTrackBySpotifyId(
      String spotifyTrackId) {
    Track track = findTrackBySpotifyId(spotifyTrackId);
    org.cosmic.backend.domain.playList.domains.Artist artist = artistRepository.findBySpotifyArtistId(
            track.artists().get(0).id())
        .orElse(artistRepository.save(
            org.cosmic.backend.domain.playList.domains.Artist.from(
                findArtistBySpotifyId(track.artists().get(0).id()))));
    org.cosmic.backend.domain.playList.domains.Album album = albumRepository.findBySpotifyAlbumId(
        track.album().id()).orElse(albumRepository.save(
        org.cosmic.backend.domain.playList.domains.Album.from(track.album(), artist)));
    return trackRepository.findBySpotifyTrackId(track.id()
    ).orElse(trackRepository.save(
        org.cosmic.backend.domain.playList.domains.Track.from(track, album, artist)));
  }
}
