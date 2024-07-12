package org.cosmic.backend.domain.post.dto.Reply;

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
}
