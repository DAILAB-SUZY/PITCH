package org.cosmic.backend.domain.albumChat.dtos.commentlike;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class AlbumChatCommentLikeIdResponse {
    //postId를 통해 이 id의 좋아요한 사람의 정보 다 가져옴
    private Long albumChatCommentId;
    private Long userId;

    public static AlbumChatCommentLikeIdResponse createAlbumChatCommentLikeIdResponse(Long albumChatCommentLikeId) {
        return  AlbumChatCommentLikeIdResponse.builder()
                .albumChatCommentId(albumChatCommentLikeId)
                .build();
    }
}