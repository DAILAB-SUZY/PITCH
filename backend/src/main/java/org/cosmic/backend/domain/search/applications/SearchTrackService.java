package org.cosmic.backend.domain.search.applications;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.cosmic.backend.domain.playList.domains.Album;
import org.cosmic.backend.domain.playList.domains.Artist;
import org.cosmic.backend.domain.playList.domains.Track;
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

@Service
public class SearchTrackService extends SearchService {

    private final TrackRepository trackRepository;
    private final ArtistRepository artistRepository;
    private final AlbumRepository albumRepository;

    public SearchTrackService(TrackRepository trackRepository, ArtistRepository artistRepository, AlbumRepository albumRepository) {
        this.trackRepository = trackRepository;
        this.artistRepository = artistRepository;
        this.albumRepository = albumRepository;
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

}
