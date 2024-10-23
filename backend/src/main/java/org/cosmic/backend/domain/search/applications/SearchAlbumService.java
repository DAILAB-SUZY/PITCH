package org.cosmic.backend.domain.search.applications;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.cosmic.backend.domain.playList.domains.Album;
import org.cosmic.backend.domain.playList.domains.Artist;
import org.cosmic.backend.domain.playList.repositorys.AlbumRepository;
import org.cosmic.backend.domain.playList.repositorys.ArtistRepository;
import org.cosmic.backend.domain.search.dtos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class SearchAlbumService extends SearchService {

  ObjectMapper mapper = new ObjectMapper();
  JsonNode rootNode = null;
  @Autowired
  private AlbumRepository albumRepository;
  @Autowired
  private ArtistRepository artistRepository;
  @Qualifier("searchService")
  @Autowired
  private SearchService searchService;

  public List<SpotifySearchAlbumResponse> searchAlbum(String accessToken, String q)
      throws JsonProcessingException {

    List<SpotifySearchAlbumResponse> spotifySearchAlbumResponses = new ArrayList<>();
    rootNode = mapper.readTree(search(accessToken, q));
    try {
      JsonNode albumitemsNode = rootNode.path("albums").path("items");

      for (int i = 0; i < albumitemsNode.size(); i++) {
        SpotifySearchAlbumResponse spotifySearchAlbumResponse = new SpotifySearchAlbumResponse();
        JsonNode item = albumitemsNode.get(i);
        String release_date = item.path("release_date").asText();
        LocalDate releaseDate = LocalDate.parse(release_date, DateTimeFormatter.ISO_DATE);
        Instant releaseDateInstant = releaseDate.atStartOfDay(ZoneId.of("UTC")).toInstant();
        spotifySearchAlbumResponse.setRelease_date(release_date);
        spotifySearchAlbumResponse.setAlbumId(item.path("id").asText());
        spotifySearchAlbumResponse.setName(item.path("name").asText());
        spotifySearchAlbumResponse.setTotal_tracks(item.path("total_tracks").asInt());

        spotifySearchAlbumResponse.setImageUrl(
            albumitemsNode.get(i).path("images").get(0).path("url").asText());
        item = albumitemsNode.get(i).path("artists");

        for (int j = 0; j < item.size(); j++) {
          SpotifySearchArtistResponse spotifySearchArtistResponse = new SpotifySearchArtistResponse();
          JsonNode artistsNode = item.get(j);
          spotifySearchArtistResponse.setArtistId(artistsNode.path("id").asText());
          spotifySearchArtistResponse.setName(artistsNode.path("name").asText());
          spotifySearchArtistResponse.setImageUrl(null);
          spotifySearchAlbumResponse.setAlbumArtist(spotifySearchArtistResponse);
        }
        spotifySearchAlbumResponses.add(spotifySearchAlbumResponse);

        String datas = searchSpotifyArtist(accessToken,
            spotifySearchAlbumResponses.get(i).getAlbumArtist().getArtistId());

        rootNode = mapper.readTree(datas);
        JsonNode artistNode = rootNode.path("images");
        String imgUrl = artistNode.get(0).path("url").asText();
        spotifySearchAlbumResponses.get(i).getAlbumArtist().setImageUrl(imgUrl);

        Artist artist;
        if (artistRepository.findBySpotifyArtistId(
            spotifySearchAlbumResponse.getAlbumArtist().getArtistId()).isPresent()) {
          artist = artistRepository.findBySpotifyArtistId(
              spotifySearchAlbumResponse.getAlbumArtist().getArtistId()).get();
        } else {
          artist = artistRepository.save(Artist.builder()
              .artistCover(spotifySearchAlbumResponse.getAlbumArtist().getImageUrl())
              .spotifyArtistId(spotifySearchAlbumResponse.getAlbumArtist().getArtistId())
              .artistName(spotifySearchAlbumResponse.getAlbumArtist().getName())
              .build());
        }

        if (albumRepository.findBySpotifyAlbumId(spotifySearchAlbumResponse.getAlbumId())
            .isEmpty()) {
          albumRepository.save(Album.builder()
              .spotifyAlbumId(spotifySearchAlbumResponse.getAlbumId())
              .title(spotifySearchAlbumResponse.getName())
              .albumCover(spotifySearchAlbumResponse.getImageUrl())
              .createdDate(releaseDateInstant)
              .artist(artist)
              .build());
        }

      }
    } catch (Exception e) {

    }
    return spotifySearchAlbumResponses; // 예외 발생 시 null 반환
  }


  public SpotifySearchAlbumResponse searchAlbumId(String accessToken, String albumId)
      throws JsonProcessingException {
    JsonNode albumItem = mapper.readTree(searchSpotifyAlbum(accessToken, albumId));
    SpotifySearchAlbumResponse spotifySearchAlbumResponse = new SpotifySearchAlbumResponse();
    spotifySearchAlbumResponse.setAlbumId(albumItem.path("id").asText());
    spotifySearchAlbumResponse.setName(albumItem.path("name").asText());
    spotifySearchAlbumResponse.setTotal_tracks(albumItem.path("total_tracks").asInt());
    spotifySearchAlbumResponse.setRelease_date(albumItem.path("release_date").asText());

    JsonNode imageNode = albumItem.path("images").get(0);
    spotifySearchAlbumResponse.setImageUrl(imageNode.path("url").asText());

    JsonNode artistNode = albumItem.path("artists").get(0);
    SpotifySearchArtistResponse spotifySearchArtistResponse = new SpotifySearchArtistResponse();
    spotifySearchArtistResponse.setName(artistNode.path("name").asText());
    spotifySearchArtistResponse.setArtistId(artistNode.path("id").asText());
    //artistId로 img가져오기
    JsonNode rootNode2 = mapper.readTree(
        searchSpotifyArtist(accessToken, artistNode.path("id").asText()));
    JsonNode artistImgNode = rootNode2.path("images");
    String imgUrl = artistImgNode.get(0).path("url").asText();
    spotifySearchArtistResponse.setImageUrl(imgUrl);
    spotifySearchAlbumResponse.setAlbumArtist(spotifySearchArtistResponse);

    LocalDate localDate = LocalDate.parse(spotifySearchAlbumResponse.getRelease_date());
    Instant instant = localDate.atStartOfDay(ZoneId.of("UTC")).toInstant();

    albumRepository.save(Album.builder()
        .spotifyAlbumId(spotifySearchAlbumResponse.getAlbumId())
        .title(spotifySearchAlbumResponse.getName())
        .albumCover(spotifySearchAlbumResponse.getImageUrl())
        .createdDate(instant)
        .artist(artistRepository.findBySpotifyArtistId(spotifySearchArtistResponse.getArtistId())
            .orElse(artistRepository.save(Artist.builder()
                .artistCover(spotifySearchArtistResponse.getImageUrl())
                .spotifyArtistId(spotifySearchArtistResponse.getArtistId())
                .artistName(spotifySearchArtistResponse.getName())
                .build())))
        .build());

    return spotifySearchAlbumResponse; // 예외 발생 시 null 반환
  }

  private Artist saveArtistByArtistDto(SpotifyArtist spotifyArtist) {
    return artistRepository.save(Artist.from(spotifyArtist));
  }

  private Album saveAlbumByAlbumDto(SpotifyAlbum spotifyAlbum,
      Artist artist) {
    return albumRepository.save(Album.from(spotifyAlbum, artist));
  }

  public void saveArtistAndAlbumBySpotifyId(String spotifyAlbumId) {
    SpotifyAlbum spotifyAlbum = searchService.findAlbumBySpotifyId(
        spotifyAlbumId);
    spotifyAlbum.artists()
        .set(0, searchService.findArtistBySpotifyId(spotifyAlbum.artists().get(0).id()));
    saveAlbumByAlbumDto(spotifyAlbum, saveArtistByArtistDto(spotifyAlbum.artists().get(0)));
  }

  public List<SpotifySearchAlbumResponse> searchAlbumByArtistIdAndAlbumName(String accessToken, String artistId, String albumName) throws JsonProcessingException { // q는 검색어
    //TODO 아티스트의 LOVE라고 했을 때 그 아티스트가 가지고 있는 LOVE관련된 것들 다 가져오기.
    List<SpotifySearchAlbumResponse> responses=new ArrayList<>();

    rootNode = mapper.readTree(searchSpotifyAlbumName(accessToken,albumName));
    JsonNode items = rootNode.path("albums").path("items");
    boolean check=false;
    for(int i=0;i<items.size();i++){
      SpotifySearchAlbumResponse response = new SpotifySearchAlbumResponse();
      System.out.println(items.get(i).path("artists"));
       if(items.get(i).path("artists").get(0).path("id").asText().equals(artistId))
       {
         JsonNode item = items.get(i);
         response.setRelease_date(item.path("release_date").asText());
         response.setAlbumId(item.path("id").asText());
         response.setName(item.path("name").asText());
         response.setTotal_tracks(item.path("total_tracks").asInt());
         response.setImageUrl(items.get(i).path("images").get(0).path("url").asText());
         item = items.get(i).path("artists");

         for (int j = 0; j < item.size(); j++) {
           SpotifySearchArtistResponse spotifySearchArtistResponse = new SpotifySearchArtistResponse();
           JsonNode artistsNode = item.get(j);
           spotifySearchArtistResponse.setArtistId(artistsNode.path("id").asText());
           spotifySearchArtistResponse.setName(artistsNode.path("name").asText());
           String datas = searchSpotifyArtist(accessToken,artistsNode.path("id").asText());
           rootNode = mapper.readTree(datas);
           JsonNode artistNode = rootNode.path("images");
           String imgUrl = artistNode.get(0).path("url").asText();
           spotifySearchArtistResponse.setImageUrl(imgUrl);
           response.setAlbumArtist(spotifySearchArtistResponse);
         }
         check=true;
       }
       if(check){
         responses.add(response);
         check=false;
       }

    }

    return responses;
  }
}
