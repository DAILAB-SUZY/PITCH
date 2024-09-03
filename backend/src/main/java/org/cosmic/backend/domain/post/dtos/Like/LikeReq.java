package org.cosmic.backend.domain.post.dtos.Like;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LikeReq {
    //postId를 통해 이 id의 좋아요한 사람의 정보 다 가져옴
    private Long user_id;
    private Long post_id;

    public static LikeReq createLikeReq(Long user_id, Long post_id) {
        return  LikeReq.builder()
                .user_id(user_id)
                .post_id(post_id)
                .build();
    }
}
