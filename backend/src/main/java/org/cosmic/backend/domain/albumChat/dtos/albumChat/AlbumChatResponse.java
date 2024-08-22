package org.cosmic.backend.domain.albumChat.dtos.albumChat;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.albumChat.domains.AlbumChat;

@Data
@NoArgsConstructor
public class AlbumChatResponse {
    private Long albumChatId;
    private String cover;
    private String title;
    private String genre;
    private String artistName;

    public AlbumChatResponse(AlbumChat albumChat) {
        this.albumChatId = albumChat.getAlbumChatId();
        this.cover = albumChat.getCover();
        this.title = albumChat.getTitle();
        this.genre = albumChat.getGenre();
        this.artistName = albumChat.getArtistName();
    }

    public AlbumChatResponse(Long albumChatId, String cover, String title, String genre, String artistName) {
        this.albumChatId = albumChatId;
        this.cover = cover;
        this.title = title;
        this.genre = genre;
        this.artistName = artistName;
    }
}
