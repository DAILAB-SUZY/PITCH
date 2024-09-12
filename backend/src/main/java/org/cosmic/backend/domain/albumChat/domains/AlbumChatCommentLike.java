package org.cosmic.backend.domain.albumChat.domains;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.user.domains.User;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="`albumChatCommentLike`")
@Builder
@IdClass(AlbumChatCommentLikePK.class)
public class AlbumChatCommentLike {

    @Id
    @ManyToOne
    @JoinColumn(name = "albumChatComment_id")
    private AlbumChatComment albumChatComment;

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
