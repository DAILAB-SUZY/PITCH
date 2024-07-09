package org.cosmic.backend.domain.post.dto.Comment;

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
}
