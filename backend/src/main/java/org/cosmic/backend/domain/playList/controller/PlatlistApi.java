package org.cosmic.backend.domain.playList.controller;

import jakarta.transaction.Transactional;
import org.cosmic.backend.domain.playList.domain.Playlist;
import org.cosmic.backend.domain.playList.dto.playlistDTO;
import org.cosmic.backend.domain.playList.dto.playlistGiveDto;
import org.cosmic.backend.domain.playList.service.PlaylistService;
import org.cosmic.backend.domain.user.dto.userDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/playlist")
public class PlatlistApi {

    @Autowired
    private PlaylistService playlistService;
    private playlistGiveDto newplaylistGiveDto = new playlistGiveDto();
    @Transactional
    @PostMapping("/give")
    public playlistGiveDto giveData(@RequestBody userDto user) {
       return playlistService.open(user.getUserid());
    }//특정 플레이어의 플레이리스트 가져와서 줌

    @PostMapping("/save")//수정한 플레이리스트를 여기 저장
    public ResponseEntity<?> saveUserDNAData(@RequestBody playlistDTO playlist) {
        // 데이터 받을 때
        Long Key= playlist.getId();
        playlistService.save(Key,playlist.getPlaylist());
        return ResponseEntity.ok("성공");//회원가입 완료 표시
    }

}
