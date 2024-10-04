package org.cosmic.backend.domain.search.applications;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import org.cosmic.backend.domain.playList.domains.Album;
import org.cosmic.backend.domain.playList.domains.Artist;
import org.cosmic.backend.domain.playList.repositorys.AlbumRepository;
import org.cosmic.backend.domain.playList.repositorys.ArtistRepository;
import org.cosmic.backend.domain.search.dtos.SpotifySearchAlbumResponse;
import org.cosmic.backend.domain.search.dtos.SpotifySearchArtistResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class SearchAlbumService extends SearchService {

  List<SpotifySearchAlbumResponse> spotifySearchAlbumResponses = new ArrayList<>();
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
    rootNode = mapper.readTree(search(accessToken, q));
    try {
      JsonNode albumitemsNode = rootNode.path("albums").path("items");

      for (int i = 0; i < albumitemsNode.size(); i++) {
        SpotifySearchAlbumResponse spotifySearchAlbumResponse = new SpotifySearchAlbumResponse();
        JsonNode item = albumitemsNode.get(i);
        spotifySearchAlbumResponse.setRelease_date(item.path("release_date").asText());
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

  private Artist saveArtistByArtistDto(org.cosmic.backend.domain.search.dtos.Artist artist) {
    return artistRepository.save(Artist.from(artist));
  }

  private Album saveAlbumByAlbumDto(org.cosmic.backend.domain.search.dtos.Album album,
      Artist artist) {
    return albumRepository.save(Album.from(album, artist));
  }

  public void saveArtistAndAlbumBySpotifyId(String spotifyAlbumId) {
    org.cosmic.backend.domain.search.dtos.Album album = searchService.findAlbumBySpotifyId(
        spotifyAlbumId);
    album.artists().set(0, searchService.findArtistBySpotifyId(album.artists().get(0).id()));
    saveAlbumByAlbumDto(album, saveArtistByArtistDto(album.artists().get(0)));
  }
}
