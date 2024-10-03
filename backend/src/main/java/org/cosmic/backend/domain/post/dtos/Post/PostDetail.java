package org.cosmic.backend.domain.post.dtos.Post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.user.dtos.UserDetail;

import java.time.Instant;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDetail {
    private Long postId;
    private String content;
    private Instant createAt;
    private Instant updateAt;
    private UserDetail author;
    private AlbumDetail album;
}
