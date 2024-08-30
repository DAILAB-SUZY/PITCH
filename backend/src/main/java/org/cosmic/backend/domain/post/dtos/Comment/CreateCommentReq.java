package org.cosmic.backend.domain.post.dtos.Comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateCommentReq {
    private Long userId;
    private Long postId;
    private String content;
    private Instant createTime;

    public static CreateCommentReq createCreateCommentReq(Long userId, Instant createTime, Long postId, String content) {
        return  CreateCommentReq.builder()
                .userId(userId)
                .createTime(createTime)
                .postId(postId)
                .content(content)
                .build();
    }
}
