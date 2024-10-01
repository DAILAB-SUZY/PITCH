package org.cosmic.backend.domain.post.dtos.Comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.user.dtos.UserDetail;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChildCommentDetail {
    private Long id;
    private String content;
    private UserDetail author;
}
