package org.cosmic.backend.domain.post.dtos.Like;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LikeDto {
    private Long userId;
    private Long postId;
    public static LikeDto createLikeDto(Long userId,Long postId) {
        return  LikeDto.builder()
                .userId(userId)
                .postId(postId)
                .build();
    }
}
