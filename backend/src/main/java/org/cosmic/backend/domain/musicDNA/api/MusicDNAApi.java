package org.cosmic.backend.domain.musicDNA.api;

import org.cosmic.backend.domain.musicDNA.applications.MusicDNAService;
import org.cosmic.backend.domain.musicDNA.domain.MusicDna;
import org.cosmic.backend.domain.musicDNA.dto.DnaDTO;
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
    public String saveUserDNAData(@RequestBody DnaDTO dna) {
        // 데이터 받을 때
        Long Key= dna.getKey();
        System.out.println(dna);
        musicDNAService.saveDNA(Key, dna.getDna());//Long
        return "success";
    }
}
