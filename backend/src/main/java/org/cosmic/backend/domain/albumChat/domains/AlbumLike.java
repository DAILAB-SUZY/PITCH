package org.cosmic.backend.domain.albumChat.domains;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.albumChat.dtos.albumlike.AlbumChatAlbumLikeDetail;
import org.cosmic.backend.domain.albumChat.dtos.commentlike.AlbumChatCommentLikeDetail;
import org.cosmic.backend.domain.playList.domains.Album;
import org.cosmic.backend.domain.user.domains.User;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name="`album_like`")
@IdClass(AlbumLikePK.class)
public class AlbumLike {

    @Id
    @ManyToOne
    @JoinColumn(name = "album_id")
    private Album album;

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "update_time")
    private Instant updateTime;

    public static AlbumChatAlbumLikeDetail toAlbumChatAlbumLikeDetail(AlbumLike albumLike) {
        return AlbumChatAlbumLikeDetail.builder()
                .author(User.toUserDetail(albumLike.user))
                .updateAt(albumLike.updateTime)
                .build();
    }
}
