package org.cosmic.backend.domain.search.applications;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.cosmic.backend.domain.playList.domains.Artist;
import org.cosmic.backend.domain.playList.dtos.Image;
import org.cosmic.backend.domain.playList.repositorys.ArtistRepository;
import org.cosmic.backend.domain.search.dtos.SpotifySearchArtistResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SearchArtistService extends SearchService {

    private final ArtistRepository artistRepository;

    public SearchArtistService(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    public void saveArtist(List<SpotifySearchArtistResponse> spotifySearchArtistResponses, String accessToken) throws JsonProcessingException {
       ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = null;
        for (int i = 0; i < spotifySearchArtistResponses.size(); i++) {
            Optional<Artist> artist = artistRepository.findBySpotifyArtistId(spotifySearchArtistResponses.get(i).getArtistId());
            if (artist.isPresent())//Local DB에 spotify_artist_id 있는지 조회
            {
                //있다면 artist_cover 채워서 보내기
                artist.get().setArtistCover(String.valueOf(spotifySearchArtistResponses.get(i).getImageUrl()));
                artistRepository.save(artist.get());
                continue;
            }//존재하면

            artistRepository.save(Artist.builder()
                .artistName(spotifySearchArtistResponses.get(i).getName())
                .artistCover(spotifySearchArtistResponses.get(i).getImageUrl())
                .spotifyArtistId(spotifySearchArtistResponses.get(i).getArtistId())
                .build());
        }
    }

    public List<SpotifySearchArtistResponse> searchArtist(String accessToken, String q) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        List<SpotifySearchArtistResponse> spotifySearchArtistResponses = new ArrayList<>();

        String data = search(accessToken, q);
        JsonNode rootNode = mapper.readTree(data);
        JsonNode artistitemsNode = rootNode.path("artists").path("items");

        for (int i = 0; i < artistitemsNode.size(); i++) {
            SpotifySearchArtistResponse spotifySearchArtistResponse = new SpotifySearchArtistResponse();
            JsonNode item = artistitemsNode.get(i);
            spotifySearchArtistResponse.setArtistId(item.path("id").asText());
            spotifySearchArtistResponse.setName(item.path("name").asText());

            // 이미지 배열이 존재하는지 확인
            JsonNode imagesNode = item.path("images");
            if (imagesNode.isArray() && imagesNode.size() > 0) {
                // 이미지 배열에 값이 있으면 첫 번째 이미지 URL 가져오기
                spotifySearchArtistResponse.setImageUrl(imagesNode.get(0).path("url").asText());
            } else {
                // 이미지가 없을 경우 null 또는 기본값 처리
                spotifySearchArtistResponse.setImageUrl(null); // 필요에 따라 기본값으로 설정 가능
            }

            spotifySearchArtistResponses.add(spotifySearchArtistResponse);
        }

        //saveArtist(spotifySearchArtistResponses,accessToken);
        return spotifySearchArtistResponses;
    }
}