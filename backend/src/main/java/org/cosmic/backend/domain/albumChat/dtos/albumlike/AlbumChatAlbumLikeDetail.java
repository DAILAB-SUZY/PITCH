package org.cosmic.backend.domain.albumChat.dtos.albumlike;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.albumChat.domains.AlbumChatComment;
import org.cosmic.backend.domain.albumChat.domains.AlbumChatCommentLike;
import org.cosmic.backend.domain.albumChat.domains.AlbumLike;
import org.cosmic.backend.domain.user.domains.User;
import org.cosmic.backend.domain.user.dtos.UserDetail;

import java.time.Instant;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlbumChatAlbumLikeDetail {
    private UserDetail author;//누가 썼는지
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSSX", timezone = "UTC")
    private Instant updateAt;

    public AlbumChatAlbumLikeDetail(AlbumLike albumLike) {
        this.updateAt = albumLike.getUpdateTime();
        this.author= User.toUserDetail(albumLike.getUser());
    }
}

