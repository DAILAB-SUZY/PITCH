package org.cosmic.backend.domain.musicDna.apis;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.transaction.Transactional;
import org.cosmic.backend.domain.musicDna.applications.MusicDnaService;
import org.cosmic.backend.domain.musicDna.dtos.DnaDto;
import org.cosmic.backend.domain.musicDna.dtos.ListDna;
import org.cosmic.backend.domain.musicDna.dtos.UserDnaResponse;
import org.cosmic.backend.domain.musicDna.exceptions.NotMatchMusicDnaCountException;
import org.cosmic.backend.domain.playList.exceptions.NotFoundUserException;
import org.cosmic.backend.domain.user.dtos.UserDto;
import org.cosmic.backend.globals.annotations.ApiCommonResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 음악 DNA 관련 API를 제공하는 REST 컨트롤러입니다.
 * 사용자와 관련된 음악 DNA 데이터를 저장하고 조회하는 기능을 포함합니다.
 */
@RestController
@RequestMapping("/api/dna")
@ApiCommonResponses
public class MusicDnaApi {
    private final MusicDnaService musicDnaService;

    /**
     * MusicDnaApi의 생성자입니다.
     *
     * @param musicDnaService 음악 DNA 관련 서비스를 처리하는 서비스 클래스
     */
    public MusicDnaApi(MusicDnaService musicDnaService) {
        this.musicDnaService = musicDnaService;
    }

    /**
     * 모든 DNA 데이터를 조회합니다.
     *
     * @return 모든 DNA 데이터를 포함하는 리스트
     */
    @Transactional
    @PostMapping("/give")
    public List<ListDna> DnaGiveData() {
        return musicDnaService.getAllDna();
    }

    /**
     * 사용자로부터 제공된 DNA 데이터를 저장합니다.
     *
     * @param dna 저장할 DNA 데이터를 포함한 DTO 객체
     * @return 저장 성공 메시지를 포함한 {@link ResponseEntity}
     *
     * @throws NotFoundUserException 유저 정보가 일치하지 않을 때 발생합니다.
     * @throws NotMatchMusicDnaCountException DNA 데이터가 4개 이상일 때 발생합니다.
     */
    @PostMapping("/save")
    @ApiResponse(responseCode = "400", description = "Need 4 MusicDna")
    @ApiResponse(responseCode = "404", description = "Not Found Emotion")
    public ResponseEntity<?> userDnaSaveData(@RequestBody DnaDto dna) {
        musicDnaService.saveDNA(dna.getKey(), dna.getDna());
        return ResponseEntity.ok("성공");
    }

    /**
     * 사용자의 DNA 데이터를 조회합니다.
     *
     * @param user 사용자 정보를 포함한 DTO 객체
     * @return 사용자 DNA 데이터를 포함한 응답 리스트
     *
     * @throws NotFoundUserException 사용자를 찾을 수 없을 때 발생합니다.
     */
    @PostMapping("/info")
    @Transactional
    @ApiResponse(responseCode = "404", description = "Not Found User")
    public List<UserDnaResponse> userDnaGive(@RequestBody UserDto user) {
        return musicDnaService.getUserDna(user);
    }
}
