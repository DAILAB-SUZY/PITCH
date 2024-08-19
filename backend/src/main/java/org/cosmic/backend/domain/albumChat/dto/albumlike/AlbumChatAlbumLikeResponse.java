package org.cosmic.backend.domain.albumChat.dto.albumlike;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlbumChatAlbumLikeResponse {
    private Long userId;
    private String userName;
    private String profilePicture;
}