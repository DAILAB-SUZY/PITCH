package org.cosmic.backend.domain.search.applications;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.cosmic.backend.domain.playList.domains.Artist;
import org.cosmic.backend.domain.playList.repositorys.ArtistRepository;
import org.cosmic.backend.domain.search.dtos.SpotifySearchArtistResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SearchArtistService extends SearchService {

    private final ArtistRepository artistRepository;
    ObjectMapper mapper = new ObjectMapper();
    JsonNode rootNode = null;
    List<SpotifySearchArtistResponse> spotifySearchArtistResponses = new ArrayList<>();
    public SearchArtistService(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    public List<SpotifySearchArtistResponse> searchArtist(String accessToken, String q) throws JsonProcessingException {
        rootNode = mapper.readTree(search(accessToken, q));
        JsonNode artistitemsNode = rootNode.path("artists").path("items");

        for (int i = 0; i < artistitemsNode.size(); i++) {
            SpotifySearchArtistResponse spotifySearchArtistResponse = new SpotifySearchArtistResponse();
            JsonNode item = artistitemsNode.get(i);
            spotifySearchArtistResponse.setArtistId(item.path("id").asText());
            spotifySearchArtistResponse.setName(item.path("name").asText());

            // 이미지 배열이 존재하는지 확인
            JsonNode imagesNode = item.path("images");
            if (imagesNode.isArray() && !imagesNode.isEmpty()) {
                // 이미지 배열에 값이 있으면 첫 번째 이미지 URL 가져오기
                spotifySearchArtistResponse.setImageUrl(imagesNode.get(0).path("url").asText());
            } else {
                spotifySearchArtistResponse.setImageUrl(null); // 필요에 따라 기본값으로 설정 가능
            }

            spotifySearchArtistResponses.add(spotifySearchArtistResponse);
        }
        return spotifySearchArtistResponses;
    }
}