package org.cosmic.backend.domain.favoriteArtist.api;

import jakarta.transaction.Transactional;
import org.cosmic.backend.domain.favoriteArtist.dto.*;
import org.cosmic.backend.domain.favoriteArtist.service.favoriteArtistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/favoriteArtist")

public class favoriteController {

    @Autowired
    private favoriteArtistService favoriteartistService;

    @PostMapping("/give")
    @Transactional
    public favoriteArtist givefavoriteArtistData(@RequestBody UserDto user) {
        // 데이터 받을 때
        return  favoriteartistService.givefavoriteArtistData(user.getUserId());//Long;
    }

    @Transactional
    @PostMapping("/searchartist")
    public List<ArtistData> searchArtistData(@RequestBody ArtistDTO artist) {//artist이름 주면
        return favoriteartistService.searchArtistData(artist.getArtistName());
    }

    //givealbum -> 해당 아티스트의 앨범들을 보여줌

    @Transactional
    @PostMapping("/searchalbum")
    public List<AlbumData> searchAlbumData(@RequestBody AlbumDTO album) {

        return favoriteartistService.searchAlbumData(album.getArtistId(),album.getAlbumName());
    }

    //givetrack -> 앨범안의 노래들을 뽑음
    @PostMapping("/searchtrack")
    @Transactional
    public List<TrackData> searchTrackData(@RequestBody TrackDTO track) {

        return favoriteartistService.searchTrackData(track.getAlbumId(),track.getTrackName());
    }

    //노래까지 다 찾으면 확인 누르면 save로

    @PostMapping("/save")
    @Transactional
    public ResponseEntity<?> savefavoriteArtistData(@RequestBody favoriteArtistDTO favoriteartist) {
        // 데이터 받을 때
        favoriteartistService.savefavoriteArtistData(favoriteartist.getUserId(),favoriteartist.getArtistId(),favoriteartist.getAlbumId(),favoriteartist.getTrackId());//Long
        return ResponseEntity.ok("성공");//회원가입 완료 표시
    }
}
