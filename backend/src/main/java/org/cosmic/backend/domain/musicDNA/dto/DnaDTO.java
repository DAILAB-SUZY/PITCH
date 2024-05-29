package org.cosmic.backend.domain.musicDNA.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DnaDTO {//사용자로부터 데이터를 받을 것임.
    private Long key;//userkey
    private List<DNADetail> dna = new ArrayList<>();
}
