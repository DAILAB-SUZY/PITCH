package org.cosmic.backend.domain.post.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.post.dtos.Comment.LikeUserDto;
import org.cosmic.backend.domain.user.domains.User;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="`post_comment_like`")
@Builder
@IdClass(PostCommentLikePK.class)
public class PostCommentLike {
    @Id
    @ManyToOne
    @JoinColumn(name = "postComment_id")
    private PostComment postComment;

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public static LikeUserDto toLikeUserDto(PostCommentLike postCommentLike) {
        return LikeUserDto.builder()
                .user_id(postCommentLike.user.getUserId())
                .build();
    }
}
