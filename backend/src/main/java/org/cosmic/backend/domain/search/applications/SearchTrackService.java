package org.cosmic.backend.domain.search.applications;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.cosmic.backend.domain.search.dtos.SpotifySearchAlbumResponse;
import org.cosmic.backend.domain.search.dtos.SpotifySearchArtistResponse;
import org.cosmic.backend.domain.search.dtos.SpotifySearchTrackResponse;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SearchTrackService extends SearchService {
    ObjectMapper mapper = new ObjectMapper();
    JsonNode rootNode;
    List<SpotifySearchTrackResponse> spotifySearchTrackResponses = new ArrayList<>();

    public List<SpotifySearchTrackResponse> searchTrack(String accessToken,String q) throws JsonProcessingException { // q는 검색어
        rootNode = mapper.readTree(search(accessToken,q));
        JsonNode trackitemsNode = rootNode.path("tracks").path("items");
        for(int i=0;i<trackitemsNode.size();i++)
        {
            SpotifySearchTrackResponse spotifySearchTrackResponse = new SpotifySearchTrackResponse();
            JsonNode item = trackitemsNode.get(i);
            spotifySearchTrackResponse.setTrackId(item.path("id").asText());
            spotifySearchTrackResponse.setTrackName(item.path("name").asText());
            int durationMs=item.path("duration_ms").asInt();
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

            String datas = searchArtistImg(accessToken,spotifySearchTrackResponses.get(i).getTrackArtist().getArtistId());

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

            datas = searchArtistImg(accessToken, spotifySearchTrackResponses.get(i).getAlbum().getAlbumArtist().getArtistId());

            rootNode = mapper.readTree(datas);
            JsonNode albumArtistNode = rootNode.path("images");
            String albumImgUrl = albumArtistNode.get(0).path("url").asText();

            spotifySearchArtistResponse.setImageUrl(albumImgUrl);
            spotifySearchTrackResponses.get(i).getAlbum().setAlbumArtist(spotifySearchArtistResponse);
        }

        return spotifySearchTrackResponses;
    }

}
