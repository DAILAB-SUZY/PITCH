package org.cosmic.backend.domain.favoriteArtist.service;
import org.cosmic.backend.domain.favoriteArtist.dto.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class favoriteArtistService {

    public List<ArtistData> searchArtistData(String artistName) {//artist이름 주면
        List<ArtistData> artistDataList = new ArrayList<>();
        ArtistData artistData=new ArtistData();
        artistData.setArtistId(1L);
        artistData.setArtistName("비비");
        artistDataList.add(artistData);
        return artistDataList;
    }

    public List<AlbumData> searchAlbumData(Long artistId,String albumName) {
        List<AlbumData> albumDataList = new ArrayList<>();
        AlbumData albumData=new AlbumData();
        albumData.setAlbumId(1L);
        albumData.setAlbumName("밤양갱");
        albumDataList.add(albumData);
        return albumDataList;
    }

    public List<TrackData> searchTrackData(Long albumId,String trackName) {
        List<TrackData> trackDataList = new ArrayList<>();
        TrackData trackData=new TrackData();
        trackData.setTrackId(1L);
        trackData.setTrackName("밤양갱");
        trackDataList.add(trackData);
        return trackDataList;
    }

    public void savefavoriteArtistData(Long userId,Long artistId,Long albumId,Long trackId) {

    }

    public favoriteArtist givefavoriteArtistData(Long userId) {
        // 데이터 받을 때
        favoriteArtist favoriteartist=new favoriteArtist();
        favoriteartist.setAlbumName("밤양갱");
        favoriteartist.setArtistName("비비");
        favoriteartist.setTrackName("밤양갱");
        favoriteartist.setCover("base");
        return favoriteartist;
    }
}
