package org.cosmic.backend.domain.musicDNA.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DNA {//사용자로부터 데이터를 받을 것임.
    private String UserEmail;
    private List<String> MusicDNA;
}
