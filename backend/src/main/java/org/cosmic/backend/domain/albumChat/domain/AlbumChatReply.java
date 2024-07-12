package org.cosmic.backend.domain.albumChat.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.user.domain.User;

import java.time.Instant;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
}
