package org.cosmic.backend.domain.albumChat.dtos.reply;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.albumChat.dtos.comment.AlbumChatCommentCreateReq;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AlbumChatReplyCreateReq {
    private Long userId;
    private Long albumChatCommentId;
    private String content;
    private Instant createTime;

    public static AlbumChatReplyCreateReq createAlbumChatReplyCreateReq(Long userId, Long albumChatCommentId, String content, Instant createTime) {
        return  AlbumChatReplyCreateReq.builder()
                .userId(userId)
                .albumChatCommentId(albumChatCommentId)
                .content(content)
                .createTime(createTime)
                .build();
    }
}
