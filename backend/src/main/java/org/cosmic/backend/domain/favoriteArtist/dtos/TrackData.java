package org.cosmic.backend.domain.favoriteArtist.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.playList.domains.Track;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TrackData {
    private Long trackId;
    private String trackName;

    public TrackData(Track track) {
        this.trackId = track.getTrackId();
        this.trackName = track.getTitle();
    }
}
