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
public class AlbumData {
    private Long albumId;
    private String trackName;

    public AlbumData(Track track) {
        this.albumId = track.getAlbum().getAlbumId();
        this.trackName = track.getTitle();
    }
}
