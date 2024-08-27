package org.cosmic.backend.domain.bestAlbum.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.bestAlbum.domains.AlbumUser;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BestAlbumGiveDto {
    Long albumId;
    String albumName;
    String cover;

    public BestAlbumGiveDto(AlbumUser albumUser) {
        this.albumId = albumUser.getAlbum().getAlbumId();
        this.albumName = albumUser.getAlbum().getTitle();
        this.cover = albumUser.getAlbum().getCover();
    }
}
