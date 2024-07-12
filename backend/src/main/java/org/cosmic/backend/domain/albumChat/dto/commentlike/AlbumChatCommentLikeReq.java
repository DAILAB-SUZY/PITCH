package org.cosmic.backend.domain.albumChat.dto.commentlike;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AlbumChatCommentLikeReq {
    //postId를 통해 이 id의 좋아요한 사람의 정보 다 가져옴
    private Long albumChatCommentLikeId;
}