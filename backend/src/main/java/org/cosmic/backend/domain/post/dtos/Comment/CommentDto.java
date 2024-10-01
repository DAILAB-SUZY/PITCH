package org.cosmic.backend.domain.post.dtos.Comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentDto {
    private Long commentId;

    public static CommentDto createCommentDto(Long commentId) {
        return CommentDto.builder().commentId(commentId).build();
    }
}
