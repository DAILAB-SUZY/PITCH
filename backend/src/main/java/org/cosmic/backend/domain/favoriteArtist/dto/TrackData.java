package org.cosmic.backend.domain.favoriteArtist.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TrackData {
    private Long trackId;
    private String trackName;
}
