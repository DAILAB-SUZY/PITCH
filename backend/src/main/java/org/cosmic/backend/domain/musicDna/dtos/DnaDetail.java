package org.cosmic.backend.domain.musicDna.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DnaDetail {
    private Long dnaKey;
    private String dnaName;
}