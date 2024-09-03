package org.cosmic.backend.domain.post.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.post.dtos.Like.LikeReq;
import org.cosmic.backend.domain.post.dtos.Like.LikeResponse;
import org.cosmic.backend.domain.user.domains.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="`post_like`")
@Builder
public class PostLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long likeId;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public static LikeResponse toLikeResponse(PostLike postLike) {
        return LikeResponse.builder()
                .userId(postLike.getUser().getUserId())
                .userName(postLike.getUser().getUsername())
                .profilePicture(postLike.getUser().getProfilePicture())
                .build();
    }

    public static LikeReq toLikeReq(PostLike postLike) {
        return LikeReq.builder()
                .likeId(postLike.likeId)
                .build();
    }
}
