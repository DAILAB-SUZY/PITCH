package org.cosmic.backend.domain.albumChat.dto.commentlike;
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
