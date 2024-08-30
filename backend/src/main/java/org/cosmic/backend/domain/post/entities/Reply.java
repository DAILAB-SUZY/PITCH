package org.cosmic.backend.domain.post.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.post.dtos.Reply.ReplyDto;
import org.cosmic.backend.domain.post.dtos.Reply.UpdateReplyReq;
import org.cosmic.backend.domain.user.domains.User;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="`reply`")
@Builder
public class Reply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long replyId;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;

    private String content;
    private Instant updateTime;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public static UpdateReplyReq toUpdateReplyReq(Reply reply) {
        return UpdateReplyReq.builder()
                .replyId(reply.getReplyId())
                .content(reply.getContent())
                .createTime(reply.getUpdateTime())
                .userId(reply.getUser().getUserId())
                .commentId(reply.getComment().getCommentId())
                .build();
    }

    public static ReplyDto toReplyDto(Reply reply) {
        return ReplyDto.builder()
                .replyId(reply.getReplyId())
                .build();
    }
}
