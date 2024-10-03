package org.cosmic.backend.domain.playList.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TrackDto {
   private String trackName;
   public static TrackDto createTrackDto(String trackName) {
      return  TrackDto.builder()
              .trackName(trackName)
              .build();
   }
}
