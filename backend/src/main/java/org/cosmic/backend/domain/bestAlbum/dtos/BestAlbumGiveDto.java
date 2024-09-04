package org.cosmic.backend.domain.bestAlbum.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.bestAlbum.domains.UserBestAlbum;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BestAlbumGiveDto {
    Long albumId;
    String albumName;
    String cover;

    public BestAlbumGiveDto(UserBestAlbum userBestAlbum) {
        this.albumId = userBestAlbum.getAlbum().getAlbumId();
        this.albumName = userBestAlbum.getAlbum().getTitle();
        this.cover = userBestAlbum.getAlbum().getCover();
    }
}
