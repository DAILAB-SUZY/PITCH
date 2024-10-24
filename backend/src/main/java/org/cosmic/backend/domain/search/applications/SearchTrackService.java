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

import java.time.Duration;
import java.time.Instant;
import java.util.*;

@Service
public class SearchTrackService extends SearchService {
    ObjectMapper mapper = new ObjectMapper();
    JsonNode rootNode;

    @Autowired
    private ArtistRepository artistRepository;
    @Autowired
    private TrackRepository trackRepository;
    @Autowired
    private AlbumRepository albumRepository;



    public List<SpotifySearchTrackResponse> searchTrack(String accessToken,String q) throws JsonProcessingException { // q는 검색어
        List<SpotifySearchTrackResponse> spotifySearchTrackResponses = new ArrayList<>();
        rootNode = mapper.readTree(search(accessToken,q));
        JsonNode trackitemsNode = rootNode.path("tracks").path("items");
        for(int i=0;i<trackitemsNode.size();i++)
        {
            SpotifySearchTrackResponse spotifySearchTrackResponse = new SpotifySearchTrackResponse();
            JsonNode item = trackitemsNode.get(i);
            spotifySearchTrackResponse.setTrackId(item.path("id").asText());
            spotifySearchTrackResponse.setTrackName(item.path("name").asText());

            int durationMs = item.path("duration_ms").asInt();
            Instant instant = Instant.EPOCH.plusMillis(durationMs);
            // 밀리초를 분과 초로 변환
            int minutes = (durationMs / 1000) / 60; // 전체 초를 60으로 나눈 값이 분
            int seconds = (durationMs / 1000) % 60; // 나머지가 초

            String formattedDuration = String.format("%d:%02d", minutes, seconds);
            spotifySearchTrackResponse.setDuration(formattedDuration);

            item = trackitemsNode.get(i).path("artists");
            SpotifySearchArtistResponse spotifySearchArtistResponse = new SpotifySearchArtistResponse();
            JsonNode artistsNode = item.get(0);
            spotifySearchArtistResponse.setArtistId(artistsNode.path("id").asText());
            spotifySearchArtistResponse.setName(artistsNode.path("name").asText());
            spotifySearchArtistResponse.setImageUrl(null);
            spotifySearchTrackResponse.setTrackArtist(spotifySearchArtistResponse);

            spotifySearchTrackResponses.add(spotifySearchTrackResponse);

            String datas = searchSpotifyArtist(accessToken,spotifySearchTrackResponses.get(i).getTrackArtist().getArtistId());

            rootNode = mapper.readTree(datas);
            JsonNode trackArtistNode = rootNode.path("images");
            String trackImgUrl = trackArtistNode.get(0).path("url").asText();
            spotifySearchArtistResponse.setImageUrl(trackImgUrl);
            spotifySearchTrackResponses.get(i).setTrackArtist(spotifySearchArtistResponse);

            SpotifySearchAlbumResponse spotifySearchAlbumResponse = new SpotifySearchAlbumResponse();
            item = trackitemsNode.get(i).path("album");
            spotifySearchAlbumResponse.setName(item.path("name").asText());
            spotifySearchAlbumResponse.setRelease_date(item.path("release_date").asText());
            spotifySearchAlbumResponse.setAlbumId(item.path("id").asText());
            spotifySearchAlbumResponse.setTotal_tracks((item.path("total_tracks").asInt()));

            spotifySearchAlbumResponse.setImageUrl(item.path("images").get(0).path("url").asText());

            item = item.path("artists");//앨범의 아티스트들.
            spotifySearchArtistResponse = new SpotifySearchArtistResponse();
            artistsNode = item.get(0);
            spotifySearchArtistResponse.setArtistId(artistsNode.path("id").asText());
            spotifySearchArtistResponse.setName(artistsNode.path("name").asText());
            spotifySearchArtistResponse.setImageUrl(null);
            spotifySearchAlbumResponse.setAlbumArtist(spotifySearchArtistResponse);
            spotifySearchTrackResponses.get(i).setAlbum(spotifySearchAlbumResponse);
            //아티스트

            datas = searchSpotifyArtist(accessToken, spotifySearchTrackResponses.get(i).getAlbum().getAlbumArtist().getArtistId());

            rootNode = mapper.readTree(datas);
            JsonNode albumArtistNode = rootNode.path("images");
            String albumImgUrl = albumArtistNode.get(0).path("url").asText();

            spotifySearchArtistResponse.setImageUrl(albumImgUrl);
            spotifySearchTrackResponses.get(i).getAlbum().setAlbumArtist(spotifySearchArtistResponse);


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

            if(albumRepository.findBySpotifyAlbumId(spotifySearchAlbumResponse.getAlbumId()).isPresent())
            {
                album = albumRepository.findBySpotifyAlbumId(spotifySearchAlbumResponse.getAlbumId()).get();
            }
            else{
                album=albumRepository.save(Album.builder()
                        .spotifyAlbumId(spotifySearchAlbumResponse.getAlbumId())
                        .title(spotifySearchAlbumResponse.getName())
                        .albumCover(spotifySearchAlbumResponse.getImageUrl())
                        .createdDate(instant)
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
        }

        return spotifySearchTrackResponses;
    }

    public List<ArtistTrackResponse> searchTrackByArtistIdAndTrackName(String accessToken, String artistId, String trackName) throws JsonProcessingException { // q는 검색어
        rootNode = mapper.readTree(searchSpotifyTrack(accessToken,trackName));
        JsonNode items = rootNode.path("tracks").path("items");
        List<ArtistTrackResponse> responses=new ArrayList<>();

        for (JsonNode item : items) {
            JsonNode artistNode = item.path("artists").get(0); // 첫 번째 아티스트 정보 사용
            if (artistNode.path("id").asText().equals(artistId)) {
                String trackId = item.path("id").asText();
                String trackNameResult = item.path("name").asText();
                String duration = item.path("duration_ms").asText();
                String imgUrl = item.path("album").path("images").get(0).path("url").asText();

                ArtistTrackResponse response = new ArtistTrackResponse();
                response.setTrackId(trackId);
                response.setTrackName(trackNameResult);
                response.setDuration(duration);
                response.setImgUrl(imgUrl);
                responses.add(response);
            }
        }
        return responses;
    }



}
