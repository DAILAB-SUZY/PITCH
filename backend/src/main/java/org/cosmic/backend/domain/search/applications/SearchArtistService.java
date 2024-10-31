package org.cosmic.backend.domain.search.applications;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.cosmic.backend.domain.playList.domains.Album;
import org.cosmic.backend.domain.playList.domains.Artist;
import org.cosmic.backend.domain.playList.domains.Track;
import org.cosmic.backend.domain.playList.repositorys.AlbumRepository;
import org.cosmic.backend.domain.playList.repositorys.ArtistRepository;
import org.cosmic.backend.domain.playList.repositorys.TrackRepository;
import org.cosmic.backend.domain.search.dtos.ArtistTrackResponse;
import org.cosmic.backend.domain.search.dtos.SpotifySearchAlbumResponse;
import org.cosmic.backend.domain.search.dtos.SpotifySearchArtistResponse;
import org.cosmic.backend.domain.search.dtos.SpotifySearchTrackResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
public class SearchArtistService extends SearchService {

    @Autowired
    private AlbumRepository albumRepository;
    @Autowired
    private ArtistRepository artistRepository;
    @Autowired
    private TrackRepository trackRepository;
    ObjectMapper mapper = new ObjectMapper();
    JsonNode rootNode = null;

    public List<SpotifySearchArtistResponse> searchArtist(String accessToken, String q) throws JsonProcessingException {

        List<SpotifySearchArtistResponse> spotifySearchArtistResponses = new ArrayList<>();
        rootNode = mapper.readTree(search(accessToken, q));
        JsonNode artistItemsNode = rootNode.path("artists").path("items");

        for (int i = 0; i < artistItemsNode.size(); i++) {
            SpotifySearchArtistResponse spotifySearchArtistResponse = new SpotifySearchArtistResponse();
            JsonNode item = artistItemsNode.get(i);
            spotifySearchArtistResponse.setArtistId(item.path("id").asText());
            spotifySearchArtistResponse.setName(item.path("name").asText());

            JsonNode imagesNode = item.path("images");
            if (imagesNode.isArray() && !imagesNode.isEmpty()) {
                // 이미지 배열에 값이 있으면 첫 번째 이미지 URL 가져오기
                spotifySearchArtistResponse.setImageUrl(imagesNode.get(0).path("url").asText());
            } else {
                spotifySearchArtistResponse.setImageUrl(null); // 필요에 따라 기본값으로 설정 가능
            }

            if(artistRepository.findBySpotifyArtistId(spotifySearchArtistResponse.getArtistId()).isEmpty())
            {
                artistRepository.save(Artist.builder()
                    .artistCover(spotifySearchArtistResponse.getImageUrl())
                    .spotifyArtistId(spotifySearchArtistResponse.getArtistId())
                    .artistName(spotifySearchArtistResponse.getName())
                    .build());
            }
            spotifySearchArtistResponses.add(spotifySearchArtistResponse);
        }
        return spotifySearchArtistResponses;
    }

    @Transactional
    public List<SpotifySearchAlbumResponse> searchAlbumByArtistId(String accessToken, String artistId) throws JsonProcessingException
    {
        List<SpotifySearchAlbumResponse> spotifySearchAlbumResponses = new ArrayList<>();
        JsonNode rootNode = mapper.readTree(searchSpotifyAlbumByArtistId(accessToken, artistId));
        JsonNode artistItem = rootNode.path("items");
        for(int i = 0; i < artistItem.size(); i++) {
            SpotifySearchAlbumResponse spotifySearchAlbumResponse = new SpotifySearchAlbumResponse();
            spotifySearchAlbumResponse.setAlbumId(artistItem.get(i).path("id").asText());
            spotifySearchAlbumResponse.setName(artistItem.get(i).path("name").asText());
            spotifySearchAlbumResponse.setTotal_tracks(artistItem.get(i).path("total_tracks").asInt());
            spotifySearchAlbumResponse.setRelease_date(artistItem.get(i).path("release_date").asText());
            JsonNode imageNode = artistItem.get(i).path("images").get(0);
            spotifySearchAlbumResponse.setImageUrl(imageNode.path("url").asText());

            JsonNode artistNode = artistItem.get(i).path("artists").get(0);
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

            Artist artist;
            Album album;
            if(artistRepository.findBySpotifyArtistId(spotifySearchArtistResponse.getArtistId()).isPresent())
            {
                artist = artistRepository.findBySpotifyArtistId(spotifySearchArtistResponse.getArtistId()).get();
            }
            else{
                artist=artistRepository.save(Artist.builder()
                        .artistCover(spotifySearchArtistResponse.getImageUrl())
                        .spotifyArtistId(spotifySearchArtistResponse.getArtistId())
                        .artistName(spotifySearchArtistResponse.getName())
                        .build());
            }

            if(albumRepository.findBySpotifyAlbumId(spotifySearchAlbumResponse.getAlbumId()).isEmpty())
            {
                albumRepository.save(Album.builder()
                        .spotifyAlbumId(spotifySearchAlbumResponse.getAlbumId())
                        .title(spotifySearchAlbumResponse.getName())
                        .albumCover(spotifySearchAlbumResponse.getImageUrl())
                        .createdDate(instant)
                        .artist(artist)
                        .build());
            }
            spotifySearchAlbumResponses.add(spotifySearchAlbumResponse);
        }
        return spotifySearchAlbumResponses; // 예외 발생 시 null 반환
    }

    @Transactional
    public List<SpotifySearchTrackResponse> searchTrackByArtistId(String accessToken, String artistId) throws JsonProcessingException
    {
        List<SpotifySearchTrackResponse> spotifySearchTrackResponses = new ArrayList<>();
        List<SpotifySearchAlbumResponse> spotifySearchAlbumResponses=searchAlbumByArtistId(accessToken,artistId);
        List<String>albumIDs=new ArrayList<>();
        for (SpotifySearchAlbumResponse spotifySearchAlbumRespons : spotifySearchAlbumResponses) {
            albumIDs.add(spotifySearchAlbumRespons.getAlbumId());
        }
        JsonNode rootNode = mapper.readTree(searchSpotifyTrackByArtistId(accessToken, albumIDs));//앨범들로 노래찾기
        JsonNode albumItem = rootNode.path("albums");

        for(int i = 0; i < albumItem.size(); i++) {
            //앨범 아티스트 정보넣기
            JsonNode albumImgNode = albumItem.get(i).path("images");
            String imgUrl = albumImgNode.get(0).path("url").asText();
            JsonNode trackItemNode = albumItem.get(i).path("tracks").path("items");

            for(int j=0; j<trackItemNode.size(); j++) {//노래정보
                SpotifySearchTrackResponse spotifySearchTrackResponse = new SpotifySearchTrackResponse();
                spotifySearchTrackResponse.setTrackArtist(spotifySearchAlbumResponses.get(i).getAlbumArtist());
                spotifySearchTrackResponse.setAlbum(spotifySearchAlbumResponses.get(i));

                spotifySearchTrackResponse.setTrackId(trackItemNode.get(j).path("id").asText());

                int durationMs = trackItemNode.get(j).path("duration_ms").asInt();
                Instant durationInstant = Instant.ofEpochMilli(durationMs);
                // 밀리초를 분과 초로 변환
                int minutes = (durationMs / 1000) / 60; // 전체 초를 60으로 나눈 값이 분
                int seconds = (durationMs / 1000) % 60; // 나머지가 초

                String formattedDuration = String.format("%d:%02d", minutes, seconds);
                spotifySearchTrackResponse.setDuration(formattedDuration);

                spotifySearchTrackResponse.setTrackName(trackItemNode.get(j).path("name").asText());

                Artist artist;
                Album album;

                if(artistRepository.findBySpotifyArtistId(spotifySearchTrackResponse.getTrackArtist().getArtistId()).isPresent())
                {
                    artist = artistRepository.findBySpotifyArtistId(spotifySearchTrackResponse.getTrackArtist().getArtistId()).get();
                }
                else{
                    artist=artistRepository.save(Artist.builder()
                            .artistCover(spotifySearchTrackResponse.getTrackArtist().getImageUrl())
                            .spotifyArtistId(spotifySearchTrackResponse.getTrackArtist().getArtistId())
                            .artistName(spotifySearchTrackResponse.getTrackArtist().getName())
                            .build());
                }

                if(albumRepository.findBySpotifyAlbumId(spotifySearchAlbumResponses.get(i).getAlbumId()).isPresent())
                {
                    album = albumRepository.findBySpotifyAlbumId(spotifySearchAlbumResponses.get(i).getAlbumId()).get();
                }
                else{
                    album=albumRepository.save(Album.builder()
                            .spotifyAlbumId(spotifySearchAlbumResponses.get(i).getAlbumId())
                            .title(spotifySearchAlbumResponses.get(i).getName())
                            .albumCover(spotifySearchAlbumResponses.get(i).getImageUrl())
                            .createdDate(durationInstant)
                            .artist(artist)
                            .build());
                }
                if(trackRepository.findBySpotifyTrackId(spotifySearchTrackResponse.getTrackId()).isEmpty())
                {
                    trackRepository.save(Track.builder()
                        .spotifyTrackId(spotifySearchTrackResponse.getTrackId())
                        .trackCover(album.getAlbumCover())
                        .artist(artist)
                        .title(spotifySearchTrackResponse.getTrackName())
                        .album(album)
                        .build());
                }
                spotifySearchTrackResponses.add(spotifySearchTrackResponse);
            }
        }

        return spotifySearchTrackResponses; // 예외 발생 시 null 반환
    }

}