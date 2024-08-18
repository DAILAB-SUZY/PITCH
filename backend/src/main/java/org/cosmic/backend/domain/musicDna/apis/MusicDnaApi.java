package org.cosmic.backend.domain.musicDna.apis;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.transaction.Transactional;
import org.cosmic.backend.domain.musicDna.applications.MusicDnaService;
import org.cosmic.backend.domain.musicDna.dtos.DnaDto;
import org.cosmic.backend.domain.musicDna.dtos.ListDna;
import org.cosmic.backend.domain.musicDna.dtos.UserDnaResponse;
import org.cosmic.backend.domain.user.dtos.UserDto;
import org.cosmic.backend.globals.annotations.ApiCommonResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dna")
@ApiCommonResponses
public class MusicDnaApi {
    private final MusicDnaService musicDnaService;

    public MusicDnaApi(MusicDnaService musicDnaService) {
        this.musicDnaService = musicDnaService;
    }

    @Transactional
    @PostMapping("/give")
    public List<ListDna> DnaGiveData() {
        return musicDnaService.getAllDna();
    }

    @PostMapping("/save")
    @ApiResponse(responseCode = "400", description = "Need 4 MusicDna")
    @ApiResponse(responseCode = "404", description = "Not Found Emotion")
    public ResponseEntity<?> userDnaSaveData(@RequestBody DnaDto dna) {
        musicDnaService.saveDNA(dna.getKey(), dna.getDna());
        return ResponseEntity.ok("성공");
    }

    @PostMapping("/info")
    @Transactional
    @ApiResponse(responseCode = "404", description = "Not Found User")
    public List<UserDnaResponse> userDnaGive(@RequestBody UserDto user) {
        return musicDnaService.getUserDna(user);
    }
}
