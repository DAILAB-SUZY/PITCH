package org.cosmic.backend.domain.albumChat.dtos.comment;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.albumChat.domains.AlbumChatComment;

import java.time.Instant;

@Data
@NoArgsConstructor
public class AlbumChatCommentResponse {
    private Long userId;
    private Long albumChatCommentId;
    private String content;
    private Instant createTime;

    public AlbumChatCommentResponse(AlbumChatComment albumChatComment) {
        this.userId = albumChatComment.getUser().getUserId();
        this.albumChatCommentId = albumChatComment.getAlbumChatCommentId();
        this.content = albumChatComment.getContent();
        this.createTime = albumChatComment.getUpdateTime();
    }

    public AlbumChatCommentResponse(Long userId,Long albumChatCommentId,String content, Instant createTime) {
        this.userId = userId;
        this.albumChatCommentId = albumChatCommentId;
        this.content = content;
        this.createTime = createTime;
    }
}
