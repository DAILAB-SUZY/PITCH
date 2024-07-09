package org.cosmic.backend.domain.post.dto.Post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePost {
    private Long postId;
    private String content;
    private Instant updateTime;
}
