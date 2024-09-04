package org.cosmic.backend.domain.albumChat.dtos.albumChat;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.playList.domains.Album;

@Data
@NoArgsConstructor
public class AlbumChatResponse {
    private Long albumId;
    private String cover;
    private String title;
    private String artistName;

    public AlbumChatResponse(Album album) {
        this.albumId = album.getAlbumId();
        this.cover = album.getCover();
        this.title = album.getTitle();
        this.artistName = album.getArtist().getArtistName();
    }

    public AlbumChatResponse(Long albumId, String cover, String title, String artistName) {
        this.albumId = albumId;
        this.cover = cover;
        this.title = title;
        this.artistName = artistName;
    }
}
