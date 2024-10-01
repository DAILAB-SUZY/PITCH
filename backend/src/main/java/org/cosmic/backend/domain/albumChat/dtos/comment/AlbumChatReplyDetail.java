package org.cosmic.backend.domain.albumChat.dtos.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.albumChat.domains.AlbumChatComment;
import org.cosmic.backend.domain.user.domains.User;
import org.cosmic.backend.domain.user.dtos.UserDetail;

import java.io.Serializable;
import java.time.Instant;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlbumChatReplyDetail implements Serializable {
    private Long albumChatCommentId;
    private String content;
    private Instant createAt;
    private Instant updateAt;
    private UserDetail author;

    public AlbumChatReplyDetail(AlbumChatComment albumChatComment) {
        this.albumChatCommentId = albumChatComment.getAlbumChatCommentId();
        this.content = albumChatComment.getContent();
        this.createAt = albumChatComment.getCreateTime();
        this.updateAt = albumChatComment.getUpdateTime();
        this.author = User.toUserDetail(albumChatComment.getUser());
    }
}