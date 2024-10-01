package org.cosmic.backend.domain.search.applications;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.cosmic.backend.domain.playList.domains.Album;
import org.cosmic.backend.domain.playList.domains.Artist;
import org.cosmic.backend.domain.playList.dtos.Image;
import org.cosmic.backend.domain.playList.repositorys.AlbumRepository;
import org.cosmic.backend.domain.playList.repositorys.ArtistRepository;
import org.cosmic.backend.domain.playList.repositorys.TrackRepository;
import org.cosmic.backend.domain.search.dtos.SpotifySearchAlbumResponse;
import org.cosmic.backend.domain.search.dtos.SpotifySearchArtistResponse;
import org.cosmic.backend.domain.search.dtos.SpotifySearchTrackResponse;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SearchAlbumService extends SearchService {

    private final TrackRepository trackRepository;
    private final ArtistRepository artistRepository;
    private final AlbumRepository albumRepository;

    public SearchAlbumService(TrackRepository trackRepository, ArtistRepository artistRepository, AlbumRepository albumRepository) {

        this.trackRepository = trackRepository;
        this.artistRepository = artistRepository;
        this.albumRepository = albumRepository;
    }
    public String searchAlbum(String accessToken, String q) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        List<SpotifySearchArtistResponse> spotifySearchArtistResponses = new ArrayList<>();
        List<SpotifySearchTrackResponse> spotifySearchTrackResponses = new ArrayList<>();

        String data=search(accessToken,q);
        JsonNode rootNode = mapper.readTree(data);
        List<SpotifySearchAlbumResponse> spotifySearchAlbumResponses = new ArrayList<>();
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
                    spotifySearchAlbumResponse.setAlbumArtist(spotifySearchArtistResponse);
                }
                spotifySearchAlbumResponses.add(spotifySearchAlbumResponse);
            }

            Artist artist1 = null;
            for(int i=0;i<spotifySearchAlbumResponses.size();i++) {
                //for (int j = 0; j < spotifySearchAlbumResponses.get(i).getAlbumArtist.size(); j++) {

                    Optional<Artist> artist = artistRepository.findBySpotifyArtistId(spotifySearchAlbumResponses.get(i).getAlbumArtist().getArtistId());
                    String datas = searchArtistImg(accessToken, spotifySearchAlbumResponses.get(i).getAlbumArtist().getArtistId());

                    rootNode = mapper.readTree(datas);
                    JsonNode artistNode = rootNode.path("images");
                    String imgUrl = artistNode.get(0).path("url").asText();

                    if (artist.isPresent())//Local DB에 spotify_artist_id 있는지 조회
                    {
                        //있다면 artist_cover 채워서 보내기
                        artist.get().setArtistCover(String.valueOf(imgUrl));
                        artistRepository.save(artist.get());
                        continue;
                    }//존재하면

                    artist1 = artistRepository.save(Artist.builder()
                            .artistName(spotifySearchAlbumResponses.get(i).getAlbumArtist().getName())
                            .artistCover(imgUrl)
                            .spotifyArtistId(spotifySearchAlbumResponses.get(i).getAlbumArtist().getArtistId())
                            .build());

                // 문자열을 LocalDate로 변환
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate localDate = LocalDate.parse(spotifySearchAlbumResponses.get(i).getRelease_date(), formatter);

                // LocalDate를 Instant로 변환 (UTC 기준)
                Instant releaseDateInstant = localDate.atStartOfDay(ZoneId.of("UTC")).toInstant();

                Optional<Album> album = albumRepository.findBySpotifyAlbumId(spotifySearchAlbumResponses.get(i).getAlbumId());
                if (!album.isPresent())//Local DB에 spotify_artist_id 있는지 조회
                {
                    Album album1 = albumRepository.save(Album.builder()
                            .albumCover(spotifySearchAlbumResponses.get(i).getImages().get(0).getUrl())
                            .title(spotifySearchAlbumResponses.get(i).getName())
                            .createdDate(releaseDateInstant)
                            .artist(artist1)
                            .spotifyAlbumId(spotifySearchAlbumResponses.get(i).getAlbumId())
                            .build()
                    );
                }
            }

        }
        catch (Exception e) {

        }
        return data; // 예외 발생 시 null 반환
    }
}
