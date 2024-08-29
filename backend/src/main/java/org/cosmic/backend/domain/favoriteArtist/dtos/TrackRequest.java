package org.cosmic.backend.domain.favoriteArtist.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TrackRequest {
    private Long albumId;
    private String trackName;
    public static TrackRequest createTrackRequest(Long albumId,String trackName) {
        return  TrackRequest.builder()
                .albumId(albumId)
                .trackName(trackName)
                .build();
    }
}
