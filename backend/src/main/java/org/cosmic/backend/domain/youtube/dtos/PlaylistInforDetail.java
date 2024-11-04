package org.cosmic.backend.domain.youtube.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlaylistInforDetail {

  private String description;
  private String title;
  private String code;
}
