package org.cosmic.backend.domain.post.entities;

import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@EqualsAndHashCode
public class PostCommentLikePK implements Serializable {
    private Long postComment;
    private Long user;
}