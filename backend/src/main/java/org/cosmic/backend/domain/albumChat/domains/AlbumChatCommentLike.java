package org.cosmic.backend.domain.albumChat.domains;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.albumChat.dtos.comment.AlbumChatCommentDetail;
import org.cosmic.backend.domain.albumChat.dtos.commentlike.AlbumChatCommentLikeDetail;
import org.cosmic.backend.domain.user.domains.User;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="`albumChatCommentLike`")
@Builder
@IdClass(AlbumChatCommentLikePK.class)
public class AlbumChatCommentLike {

    @Id
    @ManyToOne
    @JoinColumn(name = "albumChatComment_id")
    private AlbumChatComment albumChatComment;

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "update_time")
    private Instant updateTime;

    public static AlbumChatCommentLikeDetail toAlbumChatCommentLikeDetail(AlbumChatCommentLike albumChatCommentLike) {
        return AlbumChatCommentLikeDetail.builder()
                .author(User.toUserDetail(albumChatCommentLike.user))
                .updateAt(albumChatCommentLike.updateTime)
                .build();
    }
}
