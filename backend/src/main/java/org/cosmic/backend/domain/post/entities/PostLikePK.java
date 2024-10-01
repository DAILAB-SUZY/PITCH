package org.cosmic.backend.domain.post.entities;

import lombok.*;

import java.io.Serializable;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@EqualsAndHashCode
public class PostLikePK implements Serializable {
    private Long post;
    private Long user;
}
