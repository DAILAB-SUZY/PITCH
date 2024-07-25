package org.cosmic.backend.domain.albumChat.dtos.albumChat;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AlbumChatResponse {
    private Long albumChatId;
    private String cover;
    private String title;
    private String genre;
    private String artistName;

    public AlbumChatResponse(Long albumChatId, String cover, String title, String genre, String artistName) {
        this.albumChatId = albumChatId;
        this.cover = cover;
        this.title = title;
        this.genre = genre;
        this.artistName = artistName;
    }
}
