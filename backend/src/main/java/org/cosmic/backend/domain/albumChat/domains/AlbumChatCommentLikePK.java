package org.cosmic.backend.domain.albumChat.domains;

import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class AlbumChatCommentLikePK implements Serializable {
    private Long albumChatComment;
    private Long user;
}
