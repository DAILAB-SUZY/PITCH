package org.cosmic.backend.domain.search.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArtistTrackResponse {
    private String trackId;
    private String duration;
    private String trackName;
    private String imgUrl;
}
