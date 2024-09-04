package org.cosmic.backend.domain.post.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.post.dtos.Comment.CommentDto;
import org.cosmic.backend.domain.post.dtos.Comment.CommentReq;
import org.cosmic.backend.domain.user.domains.User;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "post_comment") // 테이블 이름 수정
@Builder
public class PostComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id") // 컬럼 이름 명시
    private Long commentId;

    private Long parent_comment_id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    private String content;

    @Column(name = "create_time")
    private Instant createTime;

    @Column(name = "update_time")
    private Instant updateTime;

    @OneToMany(mappedBy = "postComment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reply> replies;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public static CommentReq toCommentReq(PostComment postComment) {
        return CommentReq.builder()
                .userId(postComment.getUser().getUserId())
                .commentId(postComment.getCommentId())
                .content(postComment.getContent())
                .createTime(postComment.getUpdateTime())
                .build();
    }

    public static CommentDto toCommentDto(PostComment postComment) {
        return CommentDto.builder()
                .commentId(postComment.getCommentId())
                .build();
    }
}
