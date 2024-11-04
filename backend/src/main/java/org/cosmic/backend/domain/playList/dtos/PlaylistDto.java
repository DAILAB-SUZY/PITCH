package org.cosmic.backend.domain.playList.dtos;

import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class PlaylistDto {

  private List<Long> trackId;

  public PlaylistDto(final List<Long> trackId) {
    this.trackId = trackId;
  }
}
