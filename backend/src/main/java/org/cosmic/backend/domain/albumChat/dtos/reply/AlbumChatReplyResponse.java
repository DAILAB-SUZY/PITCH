package org.cosmic.backend.domain.albumChat.dtos.reply;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor

public class AlbumChatReplyResponse {
    private Long albumChatReplyId;
    private Long userId;
    private String content;
    private Instant createTime;
    public AlbumChatReplyResponse(Long albumChatReplyId, Long userId, String content, Instant createTime) {
        this.albumChatReplyId = albumChatReplyId;
        this.userId = userId;
        this.content = content;
        this.createTime = createTime;
    }
}
