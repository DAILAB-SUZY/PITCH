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
public class TrackDetail {
    private String trackId;
    private String trackName;
    private String trackCover;

    public TrackDetail(Track track) {
        this.trackId = track.getSpotifyTrackId();
        this.trackName = track.getTitle();
        this.trackCover=track.getTrackCover();
    }


}
