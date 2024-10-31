package org.cosmic.backend.domain.musicDna.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.musicDna.domains.MusicDna;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DnaDetail {

  private Long dnaKey;
  private String dnaName;

  static public DnaDetail from(MusicDna musicDna) {
    return DnaDetail.builder()
        .dnaKey(musicDna.getDnaId())
        .dnaName(musicDna.getName())
        .build();
  }
}