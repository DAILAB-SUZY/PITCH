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
            spotifySearchArtistResponse.setImages(images);
            spotifySearchArtistResponses.add(spotifySearchArtistResponse);
        }


        /*
        * Local DB에 spotify_artist_id 있는지 조회
        있다면 artist_cover 채워서 보내기
        없다면 spotify API 조회
        조회 결과를 Artist 테이블에 저장
        artist cover 채워서 보내기
        * */


        for (int i = 0; i < spotifySearchArtistResponses.size(); i++) {

            Optional<Artist> artist = artistRepository.findBySpotifyArtistId(spotifySearchArtistResponses.get(i).getArtistId());
            if (artist.isPresent())//Local DB에 spotify_artist_id 있는지 조회
            {
                //있다면 artist_cover 채워서 보내기
                artist.get().setArtistCover(String.valueOf(spotifySearchArtistResponses.get(i).getImages().get(0).getUrl()));
                artistRepository.save(artist.get());
                continue;
            }//존재하면


            String datas = searchArtistImg(accessToken, spotifySearchArtistResponses.get(i).getArtistId());

            rootNode = mapper.readTree(datas);
            JsonNode artistNode = rootNode.path("images");
            String imgUrl = artistNode.get(0).path("url").asText();
            Artist artist1 = artistRepository.save(Artist.builder()
                .artistName(spotifySearchArtistResponses.get(i).getName())
                .artistCover(imgUrl)
                .spotifyArtistId(spotifySearchArtistResponses.get(i).getArtistId())
                .build());
        }
        return data;
    }
}