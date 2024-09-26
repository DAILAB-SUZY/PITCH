package org.cosmic.backend.domain.bestAlbum.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.bestAlbum.domains.UserBestAlbum;

@Data
@NoArgsConstructor
public class BestAlbumDetail {
    Long albumId;
    String albumName;
    String albumCover;

    public BestAlbumDetail(UserBestAlbum userBestAlbum) {
        this.albumId = userBestAlbum.getAlbum().getAlbumId();
        this.albumName = userBestAlbum.getAlbum().getTitle();
        this.albumCover = userBestAlbum.getAlbum().getAlbumCover();
    }
    public BestAlbumDetail(Long albumId, String albumName, String cover) {
        this.albumId = albumId;
        this.albumName = albumName;
        this.albumCover = cover;
    }
}
