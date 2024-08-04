package org.cosmic.backend.domain.post.dtos.Post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostReq {
    private Long postId;
    private String cover;
    private String artistName;
    private String content;
    private String title;
    private Instant updateTime;
}