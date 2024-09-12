package org.cosmic.backend.domain.albumChat.dtos.comment;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.albumChat.domains.AlbumChatComment;

import java.time.Instant;

@Data
@NoArgsConstructor
public class AlbumChatCommentDetail {
    private Long userId;
    private Long albumChatCommentId;
    private String content;
    private Instant createTime;

    public AlbumChatCommentDetail(AlbumChatComment albumChatComment) {
        this.userId = albumChatComment.getUser().getUserId();
        this.albumChatCommentId = albumChatComment.getAlbumChatCommentId();
        this.content = albumChatComment.getContent();
        this.createTime = albumChatComment.getUpdateTime();
    }
}
