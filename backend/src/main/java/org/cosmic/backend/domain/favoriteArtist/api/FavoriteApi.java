package org.cosmic.backend.domain.favoriteArtist.api;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.transaction.Transactional;
import org.cosmic.backend.domain.favoriteArtist.dto.*;
import org.cosmic.backend.domain.favoriteArtist.service.FavoriteArtistService;
import org.cosmic.backend.domain.playList.dto.ArtistDTO;
import org.cosmic.backend.domain.user.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/favoriteArtist")

public class FavoriteApi {
    @Autowired
    private FavoriteArtistService favoriteartistService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Ok",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = String.class))
                    }),

            @ApiResponse(responseCode = "404",
                    description = "Not Found User",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))
                    }
            )
    }
    )
    @PostMapping("/give")
    @Transactional
    public FavoriteArtistDto giveFavoriteArtistData(@RequestBody UserDto user) {
        // 데이터 받을 때
        return  favoriteartistService.giveFavoriteArtistData(user.getUserId());//Long;
        //줄때 아티스트 사진도 필요할듯 싶음
    }
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Ok",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = String.class))
                    }),

            @ApiResponse(responseCode = "404",
                    description = "Not Found Artist",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))
                    }
            )
    }
    )
    @Transactional
    @PostMapping("/searchartist")
    public List<ArtistData> searchArtistData(@RequestBody ArtistDTO artist) {//artist이름 주면
        return favoriteartistService.searchArtistData(artist.getArtistName());
    }

    //givealbum -> 해당 아티스트의 앨범들을 보여줌
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Ok",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = String.class))
                    }),

            @ApiResponse(responseCode = "404",
                    description = "Not Found Album",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))
                    }
            )
    }
    )
    @Transactional
    @PostMapping("/searchalbum")
    public List<AlbumData> searchAlbumData(@RequestBody AlbumRequest album) {
        return favoriteartistService.searchAlbumData(album.getArtistId(),album.getAlbumName());
    }
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Ok",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = String.class))
                    }),

            @ApiResponse(responseCode = "404",
                    description = "Not Found Track",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))
                    }
            )
    }
    )    @PostMapping("/searchtrack")
    @Transactional
    public TrackData searchTrackData(@RequestBody TrackRequest track) {
        return favoriteartistService.searchTrackData(track.getAlbumId(),track.getTrackName());
    }
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Ok",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = String.class))
                    }),

            @ApiResponse(responseCode = "404",
                    description = "Not Found User",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))
                    }
            )
    }
    )
    //노래까지 다 찾으면 확인 누르면 save로

    @PostMapping("/save")
    @Transactional
    public ResponseEntity<?> saveFavoriteArtistData(@RequestBody FavoriteReq favoriteartist) {
        // 데이터 받을 때
        favoriteartistService.saveFavoriteArtistData(favoriteartist);
        return ResponseEntity.ok("성공");//회원가입 완료 표시
    }
}
