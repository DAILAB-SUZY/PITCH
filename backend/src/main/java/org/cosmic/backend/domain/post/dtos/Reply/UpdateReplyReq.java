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
    private String content;

    public static UpdateReplyReq createUpdateReplyReq(Long userId,Long commentId,Long replyId,String content,Instant createTime) {
        return  UpdateReplyReq.builder()
                .content(content)
                .build();
    }

}
