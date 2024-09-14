package org.cosmic.backend.domain.musicDna.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.musicDna.domains.MusicDna;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDnaResponse {
    private String dnaName;

    public UserDnaResponse(MusicDna Dna) {
        this.dnaName = Dna.getName();
    }
}
