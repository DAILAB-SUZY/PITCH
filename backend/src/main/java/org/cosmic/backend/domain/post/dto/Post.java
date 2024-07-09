package org.cosmic.backend.domain.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    private Long userId;
    private String cover;
    private String artistName;
    private String content;
    private String title;
    private Instant updateTime;
}