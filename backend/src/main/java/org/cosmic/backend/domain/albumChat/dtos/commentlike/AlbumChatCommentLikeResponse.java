package org.cosmic.backend.domain.albumChat.dtos.commentlike;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlbumChatCommentLikeResponse {
    private Long userId;
    private String userName;
    private String profilePicture;
}
