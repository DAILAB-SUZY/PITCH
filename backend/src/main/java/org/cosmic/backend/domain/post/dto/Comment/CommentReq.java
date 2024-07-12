package org.cosmic.backend.domain.post.dto.Comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentReq {
    private Long userId;
    private Long commentId;
    private String content;
    private Instant createTime;
}
