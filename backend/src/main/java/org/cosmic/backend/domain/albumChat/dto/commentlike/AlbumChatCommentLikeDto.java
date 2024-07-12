package org.cosmic.backend.domain.albumChat.dto.commentlike;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AlbumChatCommentLikeDto {
    private Long userId;
    private Long albumChatCommentId;
}
