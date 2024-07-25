package org.cosmic.backend.domain.albumChat.dtos.commentlike;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AlbumChatCommentLikeResponse {
    private Long userId;
    private String userName;
    private String profilePicture;
    public AlbumChatCommentLikeResponse(Long userId, String userName, String profilePicture) {
        this.userId = userId;
        this.userName = userName;
        this.profilePicture = profilePicture;
    }
}
