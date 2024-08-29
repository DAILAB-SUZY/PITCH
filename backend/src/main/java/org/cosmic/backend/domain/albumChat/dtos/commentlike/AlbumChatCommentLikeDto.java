package org.cosmic.backend.domain.albumChat.dtos.commentlike;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.albumChat.dtos.comment.AlbumChatCommentCreateReq;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AlbumChatCommentLikeDto {
    private Long userId;
    private Long albumChatCommentId;

    public static AlbumChatCommentLikeDto createAlbumChatCommentLikeDto(Long userId, Long albumchatCommentId) {
        return  AlbumChatCommentLikeDto.builder()
                .userId(userId)
                .albumChatCommentId(albumchatCommentId)
                .build();
    }
}
