package org.cosmic.backend.domain.favoriteArtist.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.playList.domains.Album;
import org.cosmic.backend.domain.playList.domains.Track;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AlbumDetail {
    private Long albumId;
    private String albumName;
    private String albumCover;
    //앨범 정보들.

    public AlbumDetail(Album album) {
        this.albumId = album.getAlbumId();
        this.albumCover=album.getAlbumCover();
        this.albumName=album.getTitle();
    }
}
