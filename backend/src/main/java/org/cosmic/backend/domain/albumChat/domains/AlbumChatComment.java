package org.cosmic.backend.domain.albumChat.domains;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.user.domains.User;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "albumChatComment") // 테이블 이름 수정
public class AlbumChatComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "albumChatComment_id") // 컬럼 이름 명시
    private Long albumChatCommentId;

    @ManyToOne
    @JoinColumn(name = "albumChat_id")
    private AlbumChat albumChat;

    private String content;
    private Instant updateTime;

    @OneToMany(mappedBy = "albumChatComment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AlbumChatReply> albumChatReplies;

    @OneToMany(mappedBy = "albumChatComment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AlbumChatCommentLike> albumChatCommentLikes;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public AlbumChatComment(String content, Instant updateTime, User user,List<AlbumChatReply> albumChatReply,AlbumChat albumChat) {
        this.albumChat = albumChat;
        this.content = content;
        this.updateTime = updateTime;
        this.user=user;
        this.albumChatReplies = albumChatReply;
    }

    @Override
    public String toString() {
        return "AlbumChatComment{" +
                "albumChatCommentId=" + albumChatCommentId +
                ", content='" + content + '\'' +
                ", updateTime=" + updateTime +
                ", user=" + (user != null ? user.getUserId() : "null") +
                '}';
    }
}
