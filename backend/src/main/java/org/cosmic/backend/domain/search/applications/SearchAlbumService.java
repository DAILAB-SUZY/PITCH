package org.cosmic.backend.domain.search.applications;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.cosmic.backend.domain.search.dtos.SpotifySearchAlbumResponse;
import org.cosmic.backend.domain.search.dtos.SpotifySearchArtistResponse;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class SearchAlbumService extends SearchService {
    List<SpotifySearchAlbumResponse> spotifySearchAlbumResponses = new ArrayList<>();
    ObjectMapper mapper = new ObjectMapper();
    JsonNode rootNode = null;

    public List<SpotifySearchAlbumResponse> searchAlbum(String accessToken, String q) throws JsonProcessingException {
        rootNode = mapper.readTree(search(accessToken,q));
        try {
            JsonNode albumitemsNode = rootNode.path("albums").path("items");

            for (int i = 0; i < albumitemsNode.size(); i++) {
                SpotifySearchAlbumResponse spotifySearchAlbumResponse = new SpotifySearchAlbumResponse();
                JsonNode item = albumitemsNode.get(i);
                spotifySearchAlbumResponse.setRelease_date(item.path("release_date").asText());
                spotifySearchAlbumResponse.setAlbumId(item.path("id").asText());
                spotifySearchAlbumResponse.setName(item.path("name").asText());
                spotifySearchAlbumResponse.setTotal_tracks(item.path("total_tracks").asInt());

                spotifySearchAlbumResponse.setImageUrl(albumitemsNode.get(i).path("images").get(0).path("url").asText());
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

                String datas = searchArtistImg(accessToken, spotifySearchAlbumResponses.get(i).getAlbumArtist().getArtistId());

                rootNode = mapper.readTree(datas);
                JsonNode artistNode = rootNode.path("images");
                String imgUrl = artistNode.get(0).path("url").asText();
                spotifySearchAlbumResponses.get(i).getAlbumArtist().setImageUrl(imgUrl);
            }
        }
        catch (Exception e) {

        }
        return spotifySearchAlbumResponses; // 예외 발생 시 null 반환
    }
}
