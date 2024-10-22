package org.cosmic.backend.domain.playList.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.playList.domains.Track;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TrackDetail {

  String title;
  String artistName;

  public static TrackDetail from(Track track) {
    return TrackDetail.builder()
        .title(track.getTitle())
        .artistName(track.getArtist().getArtistName())
        .build();
  }
}
