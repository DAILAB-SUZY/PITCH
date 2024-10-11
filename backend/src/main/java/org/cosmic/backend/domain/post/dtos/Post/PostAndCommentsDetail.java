package org.cosmic.backend.domain.post.dtos.Post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.post.dtos.Comment.CommentDetail;
import org.cosmic.backend.domain.user.dtos.UserDetail;

import java.time.Instant;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostAndCommentsDetail {
    private PostDetail postDetail;
    private List<CommentDetail> comments;
    private List<UserDetail> likes;
}
