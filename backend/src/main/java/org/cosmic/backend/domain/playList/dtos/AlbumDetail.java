package org.cosmic.backend.domain.playList.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.playList.domains.Album;

@Data
@NoArgsConstructor
public class AlbumDetail {
    private Long albumId;
    private String cover;
    private String title;
    private String artistName;

    public AlbumDetail(Album album) {
        this.albumId = album.getAlbumId();
        this.cover = album.getAlbumCover();
        this.title = album.getTitle();
        this.artistName = album.getArtist().getArtistName();
    }
}
