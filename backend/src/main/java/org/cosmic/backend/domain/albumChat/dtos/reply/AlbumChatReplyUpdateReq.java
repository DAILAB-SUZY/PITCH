package org.cosmic.backend.domain.albumChat.dtos.reply;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlbumChatReplyUpdateReq {
    private Long userId;
    private Long albumChatReplyId;
    private Long albumChatCommentId;
    private String content;
    private Instant createTime;

    public static AlbumChatReplyUpdateReq createAlbumChatReplyUpdateReq(Long userId, Long albumChatCommentId, Long albumChatReplyId, String content, Instant createTime) {
        return  AlbumChatReplyUpdateReq.builder()
                .userId(userId)
                .albumChatCommentId(albumChatCommentId)
                .albumChatReplyId(albumChatReplyId)
                .content(content)
                .createTime(createTime)
                .build();
    }
}
