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
public class UpdateReplyReq {
    private Long userId;
    private Long replyId;
    private Long commentId;
    private String content;
    private Instant createTime;

    public static UpdateReplyReq createUpdateReplyReq(Long userId,Long commentId,Long replyId,String content,Instant createTime) {
        return  UpdateReplyReq.builder()
                .userId(userId)
                .commentId(commentId)
                .replyId(replyId)
                .content(content)
                .createTime(createTime)
                .build();
    }

}
