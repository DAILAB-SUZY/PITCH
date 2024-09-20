package org.cosmic.backend.domain.post.dtos.Comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.user.dtos.UserDetail;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDetail {
    private Long id;
    private String content;
    private Instant createdAt;
    private Instant updatedAt;
    private List<LikeUserDto> likes;
    private UserDetail author;
}
