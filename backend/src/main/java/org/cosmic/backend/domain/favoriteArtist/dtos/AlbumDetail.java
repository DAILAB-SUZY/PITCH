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
public class AlbumDetail {
    private Long albumId;
    private String trackName;
    //앨범 정보들.

    public AlbumDetail(Track track) {
        this.albumId = track.getAlbum().getAlbumId();
        this.trackName = track.getTitle();
    }
}
