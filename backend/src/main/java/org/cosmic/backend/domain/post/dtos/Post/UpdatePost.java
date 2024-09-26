package org.cosmic.backend.domain.post.dtos.Post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePost {
    private Long postId;
    private String content;
    public static UpdatePost createUpdatePost(Long postId, String content) {
        return  UpdatePost.builder()
                .postId(postId)
                .content(content)
                .build();
    }
}

