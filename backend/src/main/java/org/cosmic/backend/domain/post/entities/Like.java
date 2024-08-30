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
@Table(name="`like`")
@Builder
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long likeId;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public static LikeResponse toLikeResponse(Like like) {
        return LikeResponse.builder()
                .userId(like.getUser().getUserId())
                .userName(like.getUser().getUsername())
                .profilePicture(like.getUser().getProfilePicture())
                .build();
    }

    public static LikeReq toLikeReq(Like like) {
        return LikeReq.builder()
                .likeId(like.likeId)
                .build();
    }
}
