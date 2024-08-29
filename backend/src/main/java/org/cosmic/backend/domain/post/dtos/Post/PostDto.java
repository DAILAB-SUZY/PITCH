package org.cosmic.backend.domain.post.dtos.Post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostDto {
    private Long postId;

    public static PostDto createPostDto(Long postId) {
        return  PostDto.builder()
                .postId(postId)
                .build();
    }
}
