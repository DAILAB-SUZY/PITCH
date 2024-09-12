package org.cosmic.backend.domain.albumChat.dtos.albumChat;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.playList.domains.Album;

@Data
@NoArgsConstructor
public class AlbumChatDetail {
    private Long albumId;
    private String cover;
    private String title;
    private String artistName;

    public AlbumChatDetail(Album album) {
        this.albumId = album.getAlbumId();
        this.cover = album.getCover();
        this.title = album.getTitle();
        this.artistName = album.getArtist().getArtistName();
    }
}
