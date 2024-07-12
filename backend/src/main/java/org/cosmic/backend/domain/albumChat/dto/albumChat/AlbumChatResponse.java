package org.cosmic.backend.domain.albumChat.dto.albumChat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlbumChatResponse {
    private Long albumChatId;
    private String cover;
    private String title;
    private String genre;
    private String artistName;
}
