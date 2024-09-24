package org.cosmic.backend.domain.bestAlbum.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.playList.domains.Album;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlbumInfoDetail {
    String title;
    String artistName;
    String cover;

    public AlbumInfoDetail(Album album) {
        this.title = album.getTitle();
        this.artistName = album.getArtist().getArtistName();
        this.cover = album.getCover();
    }
}
