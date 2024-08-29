package org.cosmic.backend.domain.post.dtos.Reply;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateReplyReq {
    private Long userId;
    private Long commentId;
    private String content;
    private Instant createTime;

    public static CreateReplyReq createCreateReplyReq(Long userId, Long commentId, String content, Instant createTime) {
        return  CreateReplyReq.builder()
                .userId(userId)
                .commentId(commentId)
                .content(content)
                .createTime(createTime)
                .build();
    }
}
