package org.cosmic.backend.domain.post.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.post.dtos.Comment.CommentDto;
import org.cosmic.backend.domain.post.dtos.Comment.CommentReq;
import org.cosmic.backend.domain.post.dtos.Reply.ReplyDto;
import org.cosmic.backend.domain.post.dtos.Reply.UpdateReplyReq;
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

    @ManyToOne(fetch = FetchType.LAZY)
    private PostComment parentComment;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    private String content;

    @Column(name = "create_time")
    @Builder.Default
    private Instant createTime = Instant.now();

    @Column(name = "update_time")
    private Instant updateTime;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "postComment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostCommentLike> postCommentLikes;

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

    public static UpdateReplyReq toUpdateReplyReq(PostComment postComment) {
        return UpdateReplyReq.builder()
                .replyId(postComment.getCommentId())
                .commentId(postComment.getParentComment().getCommentId())
                .content(postComment.getContent())
                .userId(postComment.getUser().getUserId())
                .content(postComment.getContent())
                .build();
    }

    public static ReplyDto toReplyDto(PostComment postComment) {
        return ReplyDto.builder()
                .replyId(postComment.commentId)
                .build();
    }
}
