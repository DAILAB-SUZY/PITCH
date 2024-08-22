package org.cosmic.backend.domain.albumChat.dtos.commentlike;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.albumChat.domains.AlbumChatCommentLike;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlbumChatCommentLikeResponse {
    private Long userId;
    private String userName;
    private String profilePicture;

    public AlbumChatCommentLikeResponse(AlbumChatCommentLike albumChatCommentLike) {
        this.userId = albumChatCommentLike.getUser().getUserId();
        this.userName = albumChatCommentLike.getUser().getUsername();
        this.profilePicture = albumChatCommentLike.getUser().getProfilePicture();
    }
}
