package org.cosmic.backend.domain.albumChat.domains;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.user.domain.User;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="`albumChatCommentLike`")
public class AlbumChatCommentLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long albumChatCommentLikeId;

    @ManyToOne
    @JoinColumn(name = "albumChatComment_id")
    private AlbumChatComment albumChatComment;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
