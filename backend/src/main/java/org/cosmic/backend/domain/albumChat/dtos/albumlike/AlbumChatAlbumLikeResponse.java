package org.cosmic.backend.domain.albumChat.dtos.albumlike;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.albumChat.domains.AlbumLike;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlbumChatAlbumLikeResponse {
    private Long userId;
    private String userName;
    private String profilePicture;

    public AlbumChatAlbumLikeResponse(AlbumLike albumLike) {
        this.userId = albumLike.getUser().getUserId();
        this.userName = albumLike.getUser().getUsername();
        this.profilePicture = albumLike.getUser().getProfilePicture();
    }
}

