package org.cosmic.backend.domain.musicDNA.api;

import org.cosmic.backend.domain.musicDNA.applications.MusicDNAService;
import org.cosmic.backend.domain.musicDNA.domain.MusicDna;
import org.cosmic.backend.domain.musicDNA.dto.DNA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dna")
public class MusicDNAApi {
    @Autowired
    private MusicDNAService musicDNAService;

    @PostMapping("/give")
    public List<MusicDna> giveDNAData() {
        return musicDNAService.getAllDna();
    }

    @PostMapping("/save")
    public String saveUserDNAData(@RequestBody DNA dna) {
        // 데이터 받을 때
        String userEmail= dna.getUserEmail();
        musicDNAService.saveDNA(userEmail, dna.getMusicDNA());
        return "success";
    }
}