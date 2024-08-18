package org.cosmic.backend.domain.musicDna.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DnaDto {//사용자로부터 데이터를 받을 것임.
    private Long key;
    private List<DnaDetail> dna;
}
