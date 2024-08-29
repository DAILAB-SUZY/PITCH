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
public class UpdateCommentReq {
    private Long userId;
    private Long postId;
    private Long commentId;
    private String content;
    private Instant createTime;

    public static UpdateCommentReq createUpdateCommentReq(Long userId,Instant createTime,Long postId,Long commentId,String content) {
        return  UpdateCommentReq.builder()
                .userId(userId)
                .createTime(createTime)
                .postId(postId)
                .commentId(commentId)
                .content(content)
                .build();
    }
}
