package org.cosmic.backend.domain.post.dtos.Post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostReq {
    private Long postId;
    private String content;
    private Instant update_time;
}
