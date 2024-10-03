package org.cosmic.backend.domain.search.applications;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.cosmic.backend.domain.favoriteArtist.dtos.ArtistDetail;
import org.cosmic.backend.domain.favoriteArtist.dtos.TrackDetail;
import org.cosmic.backend.domain.playList.domains.Album;
import org.cosmic.backend.domain.playList.domains.Artist;
import org.cosmic.backend.domain.playList.domains.Track;
import org.cosmic.backend.domain.playList.dtos.Image;
import org.cosmic.backend.domain.playList.exceptions.NotFoundArtistException;
import org.cosmic.backend.domain.playList.exceptions.NotFoundTrackException;
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
import java.util.*;

@Service
public class SearchTrackService extends SearchService {

    private final TrackRepository trackRepository;
    private final ArtistRepository artistRepository;
    private final AlbumRepository albumRepository;
    Artist artist1=null;
    Artist artist2=null;
    Album album1=null;
    Track track1=null;
    ObjectMapper mapper = new ObjectMapper();
    JsonNode rootNode;
    public SearchTrackService(TrackRepository trackRepository, ArtistRepository artistRepository, AlbumRepository albumRepository) {
        this.trackRepository = trackRepository;
        this.artistRepository = artistRepository;
        this.albumRepository = albumRepository;
    }

    public void saveTrack(String accessToken,List<SpotifySearchTrackResponse> spotifySearchTrackResponses) throws JsonProcessingException {
        for(int i=0;i<spotifySearchTrackResponses.size();i++)
        {
            //trackArtist
            Optional<Artist> artist = artistRepository.findBySpotifyArtistId(spotifySearchTrackResponses.get(i).getTrackArtist().getArtistId());
            String datas = searchArtistImg(accessToken,spotifySearchTrackResponses.get(i).getTrackArtist().getArtistId());

            rootNode = mapper.readTree(datas);
            JsonNode trackArtistNode = rootNode.path("images");
            String trackImgUrl = trackArtistNode.get(0).path("url").asText();

            if (artist.isPresent())//Local DB에 spotify_artist_id 있는지 조회
            {
                //있다면 artist_cover 채워서 보내기
                artist.get().setArtistCover(String.valueOf(trackImgUrl));
                artist1 =artistRepository.save(artist.get());
            }//존재하면
            else {
                artist1 = artistRepository.save(Artist.builder()
                        .artistName(spotifySearchTrackResponses.get(i).getTrackName())
                        .artistCover(trackImgUrl)
                        .spotifyArtistId(spotifySearchTrackResponses.get(i).getTrackArtist().getArtistId())
                        .build());
            }

            //albumArtist
            artist = artistRepository.findBySpotifyArtistId(String.valueOf(spotifySearchTrackResponses.get(i).getAlbum().getAlbumArtist().getArtistId()));
            datas = searchArtistImg(accessToken, spotifySearchTrackResponses.get(i).getAlbum().getAlbumArtist().getArtistId());

            rootNode = mapper.readTree(datas);
            JsonNode albumArtistNode = rootNode.path("images");
            String albumImgUrl = albumArtistNode.get(0).path("url").asText();

            if (artist.isPresent())//Local DB에 spotify_artist_id 있는지 조회
            {
                //있다면 artist_cover 채워서 보내기
                artist.get().setArtistCover(String.valueOf(albumImgUrl));
                Artist artist2=artistRepository.save(artist.get());
            }//존재하면

            else{
                artist2=artistRepository.save(Artist.builder()
                        .artistName(spotifySearchTrackResponses.get(i).getAlbum().getAlbumArtist().getName())
                        .artistCover(albumImgUrl)
                        .spotifyArtistId(spotifySearchTrackResponses.get(i).getAlbum().getAlbumArtist().getArtistId())
                        .build());
            }

            // 문자열을 LocalDate로 변환
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate localDate = LocalDate.parse(spotifySearchTrackResponses.get(i).getAlbum().getRelease_date(), formatter);

            // LocalDate를 Instant로 변환 (UTC 기준)
            Instant releaseDateInstant = localDate.atStartOfDay(ZoneId.of("UTC")).toInstant();

            Optional<Album> album = albumRepository.findBySpotifyAlbumId(spotifySearchTrackResponses.get(i).getAlbum().getAlbumId());
            if (album.isEmpty())//Local DB에 spotify_artist_id 있는지 조회
            {
                Album album1 = albumRepository.save(Album.builder()
                        .albumCover(spotifySearchTrackResponses.get(i).getAlbum().getImageUrl())
                        .title(spotifySearchTrackResponses.get(i).getAlbum().getName())
                        .createdDate(releaseDateInstant)
                        .artist(artist1)
                        .spotifyAlbumId(spotifySearchTrackResponses.get(i).getAlbum().getAlbumId())
                        .build()
                );
            }

            Optional<Track> track = trackRepository.findBySpotifyTrackId(spotifySearchTrackResponses.get(i).getTrackId());
            if (track.isEmpty())//Local DB에 spotify_artist_id 있는지 조회
            {
                track1=trackRepository.save(Track.builder()
                        .album(album1)
                        .title(spotifySearchTrackResponses.get(i).getTrackName())
                        .artist(artist1)
                        .trackCover(spotifySearchTrackResponses.get(i).getAlbum().getImageUrl())
                        .spotifyTrackId(spotifySearchTrackResponses.get(i).getTrackId())
                        .build()
                );
            }


        }
    }
    public List<SpotifySearchTrackResponse> searchTrack(String accessToken,String q) throws JsonProcessingException { // q는 검색어

        /*List<TrackDetail> trackDetails = new ArrayList<>();
        //아티스트, 노래 이름으로 앨범, 노래, 아티스트 모든 정보 주기.
        //q가 노래 이름인지 아티스트 이름인지 알 수 없으니까 q로
        //만약 노래 이름을 줬다면 해당 track에서 해당 노래이름과 동일한 얘들만 뽑아서 앨범정보와 아티스트 정보
        //만약 아티스트 이름을 줬다면 해당 아티스트 이름을 가진 track들을 가져옴

        if (!trackRepository.findByArtist_ArtistName(q).isEmpty()) {
            System.out.println("*******");
            trackDetails=trackRepository.findByArtist_ArtistName(q)
                .stream()
                .map(TrackDetail::new)
                .toList();
        }
        System.out.println("*******");

        if (!trackRepository.findByTitle(q).isEmpty()) {
            Set<TrackDetail> trackDetailSet = new HashSet<>(trackDetails); // 중복 방지를 위한 Set

            List<TrackDetail> newTrackDetails = trackRepository.findByTitle(q)
                    .stream()
                    .map(TrackDetail::new)
                    .filter(trackDetailSet::add) // 중복 방지: 추가되지 않는 경우는 중복된 경우
                    .toList();

            trackDetails.addAll(newTrackDetails); // 새로운 트랙만 추가
        }
        System.out.println(trackDetails);*/

        //DB에서 찾기



        List<SpotifySearchTrackResponse> spotifySearchTrackResponses = new ArrayList<>();

        String data=search(accessToken,q);
        JsonNode rootNode = mapper.readTree(data);
        JsonNode trackitemsNode = rootNode.path("tracks").path("items");

        for(int i=0;i<trackitemsNode.size();i++)
        {
            SpotifySearchTrackResponse spotifySearchTrackResponse = new SpotifySearchTrackResponse();
            JsonNode item = trackitemsNode.get(i);
            spotifySearchTrackResponse.setTrackId(item.path("id").asText());
            spotifySearchTrackResponse.setTrackName(item.path("name").asText());

            item = trackitemsNode.get(i).path("artists");
            SpotifySearchArtistResponse spotifySearchArtistResponse = new SpotifySearchArtistResponse();
            JsonNode artistsNode = item.get(0);
            spotifySearchArtistResponse.setArtistId(artistsNode.path("id").asText());
            spotifySearchArtistResponse.setName(artistsNode.path("name").asText());
            spotifySearchArtistResponse.setImageUrl(null);
            spotifySearchTrackResponse.setTrackArtist(spotifySearchArtistResponse);

            spotifySearchTrackResponses.add(spotifySearchTrackResponse);






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
            spotifySearchAlbumResponse.setImageUrl(item.path("images").get(0).path("url").asText());



            item = item.path("artists");//앨범의 아티스트들.
            spotifySearchArtistResponse = new SpotifySearchArtistResponse();
            artistsNode = item.get(0);
            spotifySearchArtistResponse.setArtistId(artistsNode.path("id").asText());
            spotifySearchArtistResponse.setName(artistsNode.path("name").asText());
            spotifySearchArtistResponse.setImageUrl(null);
            spotifySearchAlbumResponse.setAlbumArtist(spotifySearchArtistResponse);

            spotifySearchTrackResponse.setAlbum(spotifySearchAlbumResponse);
            spotifySearchTrackResponses.add(spotifySearchTrackResponse);
            //아티스트
        }

        //saveTrack(accessToken,spotifySearchTrackResponses);

        return spotifySearchTrackResponses;
    }

}
