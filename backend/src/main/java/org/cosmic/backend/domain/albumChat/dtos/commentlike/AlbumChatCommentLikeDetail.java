package org.cosmic.backend.domain.albumChat.dtos.commentlike;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.albumChat.domains.AlbumChatCommentLike;
import org.cosmic.backend.domain.albumChat.domains.AlbumLike;
import org.cosmic.backend.domain.user.domains.User;
import org.cosmic.backend.domain.user.dtos.UserDetail;

import java.time.Instant;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlbumChatCommentLikeDetail {
    private UserDetail author;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSSX", timezone = "UTC")
    private Instant updateAt;

    public AlbumChatCommentLikeDetail(AlbumChatCommentLike albumChatCommentLike) {
        this.updateAt = albumChatCommentLike.getUpdateTime();
        this.author= User.toUserDetail(albumChatCommentLike.getUser());
    }
}