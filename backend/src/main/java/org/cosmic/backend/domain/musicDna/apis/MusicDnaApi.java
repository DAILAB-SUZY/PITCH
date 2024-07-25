package org.cosmic.backend.domain.musicDna.apis;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.transaction.Transactional;
import org.cosmic.backend.domain.musicDna.applications.MusicDnaService;
import org.cosmic.backend.domain.musicDna.dtos.DnaDto;
import org.cosmic.backend.domain.musicDna.dtos.ListDna;
import org.cosmic.backend.domain.musicDna.dtos.UserDnaResponse;
import org.cosmic.backend.domain.user.dtos.UserDto;
import org.cosmic.backend.globals.dto.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dna")
public class MusicDnaApi {
    @Autowired
    private MusicDnaService musicDnaService;

    @Transactional
    @PostMapping("/give")
    public List<ListDna> DnaGiveData() {
        return musicDnaService.getAllDna();
    }

    @PostMapping("/save")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
            description = "Ok",
            content = {
                @Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class))
            }),
        @ApiResponse(responseCode = "400",
            description = "Need 4 MusicDna",
            content = {
                @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))
            }
        ),
        @ApiResponse(responseCode = "404",
            description = "Not Found Emotion",
            content = {
                @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))
            }
        )
        }
    )
    public ResponseEntity<?> userDnaSaveData(@RequestBody DnaDto dna) {
        musicDnaService.saveDNA(dna.getKey(), dna.getDna());//Long
        return ResponseEntity.ok("성공");//회원가입 완료 표시
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
    @PostMapping("/info")
    @Transactional
    public List<UserDnaResponse> userDnaGive(@RequestBody UserDto user) {
        return musicDnaService.getUserDna(user);
    }
}
