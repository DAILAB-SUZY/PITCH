package org.cosmic.backend.domain.albumChat.dtos.reply;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.albumChat.domains.AlbumChatReply;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlbumChatReplyResponse {
    private Long albumChatReplyId;
    private Long userId;
    private String content;
    private Instant createTime;

    public AlbumChatReplyResponse(AlbumChatReply albumChatReply) {
        this.albumChatReplyId = albumChatReply.getAlbumChatReplyId();
        this.userId = albumChatReply.getUser().getUserId();
        this.content = albumChatReply.getContent();
        this.createTime = albumChatReply.getUpdateTime();
    }
}
