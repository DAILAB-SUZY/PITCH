package org.cosmic.backend.domain.albumChat.dtos.albumlike;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.albumChat.domains.AlbumChatAlbumLike;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlbumChatAlbumLikeResponse {
    private Long userId;
    private String userName;
    private String profilePicture;

    public AlbumChatAlbumLikeResponse(AlbumChatAlbumLike albumChatAlbumLike) {
        this.userId = albumChatAlbumLike.getUser().getUserId();
        this.userName = albumChatAlbumLike.getUser().getUsername();
        this.profilePicture = albumChatAlbumLike.getUser().getProfilePicture();
    }
}

