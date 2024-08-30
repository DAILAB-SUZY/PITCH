package org.cosmic.backend.domain.albumChat.domains;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.user.domains.User;

import java.time.Instant;

@Data
@NoArgsConstructor
@Entity
@Table(name="`albumChatReply`")
public class AlbumChatReply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long albumChatReplyId;

    @ManyToOne
    @JoinColumn(name = "albumchatComment_id")
    private AlbumChatComment albumChatComment;

    private String content;
    private Instant updateTime;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public AlbumChatReply(String content, Instant updateTime,AlbumChatComment albumChatComment, User user) {
        this.albumChatComment = albumChatComment;
        this.content = content;
        this.updateTime = updateTime;
        this.user = user;
    }
}
