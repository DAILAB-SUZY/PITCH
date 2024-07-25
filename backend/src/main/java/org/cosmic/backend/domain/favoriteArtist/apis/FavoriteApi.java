package org.cosmic.backend.domain.favoriteArtist.apis;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.transaction.Transactional;
import org.cosmic.backend.domain.favoriteArtist.applications.FavoriteArtistService;
import org.cosmic.backend.domain.favoriteArtist.dtos.*;
import org.cosmic.backend.domain.playList.dto.ArtistDto;
import org.cosmic.backend.domain.user.dtos.UserDto;
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
    public FavoriteArtistDto favoriteArtistGiveData(@RequestBody UserDto user) {
        return  favoriteartistService.favoriteArtistGiveData(user.getUserId());
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
    public List<ArtistData> artistSearchData(@RequestBody ArtistDto artist) {//artist이름 주면
        return favoriteartistService.artistSearchData(artist.getArtistName());
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
    public List<AlbumData> albumSearchData(@RequestBody AlbumRequest album) {
        return favoriteartistService.albumSearchData(album.getArtistId(),album.getAlbumName());
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
    )
    @PostMapping("/searchtrack")
    @Transactional
    public TrackData trackSearchData(@RequestBody TrackRequest track) {
        return favoriteartistService.trackSearchData(track.getAlbumId(),track.getTrackName());
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

    @PostMapping("/save")
    @Transactional
    public ResponseEntity<?> favoriteArtistSaveData(@RequestBody FavoriteReq favoriteartist) {
        favoriteartistService.favoriteArtistSaveData(favoriteartist);
        return ResponseEntity.ok("성공");//회원가입 완료 표시
    }
}
