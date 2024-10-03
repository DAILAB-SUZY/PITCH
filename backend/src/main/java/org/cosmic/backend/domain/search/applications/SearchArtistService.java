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

    public String searchArtist(String accessToken, String q) throws JsonProcessingException {
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
            spotifySearchArtistResponse.setImageUrl(artistitemsNode.get(i).path("images").get(0).path("url").asText());
            spotifySearchArtistResponses.add(spotifySearchArtistResponse);
        }

        saveArtist(spotifySearchArtistResponses,accessToken);
        return data;
    }
}