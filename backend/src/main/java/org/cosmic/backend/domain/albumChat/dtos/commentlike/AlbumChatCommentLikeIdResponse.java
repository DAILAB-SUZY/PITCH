package org.cosmic.backend.domain.albumChat.dtos.commentlike;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
public class AlbumChatCommentLikeIdResponse {
    //postId를 통해 이 id의 좋아요한 사람의 정보 다 가져옴
    private Long albumChatCommentLikeId;
    public AlbumChatCommentLikeIdResponse(Long albumChatCommentLikeId) {
        this.albumChatCommentLikeId = albumChatCommentLikeId;
    }
}