package org.cosmic.backend.domain.post.dtos.Post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreatePost {
    private String content;
    private String title;
    private Long albumId;

    public static CreatePost createCreatePost(Long userId, String cover, String artistName, String content, String title, Instant updateTime) {
        return  CreatePost.builder()
                .content(content)
                .title(title)
                .build();
    }
}
