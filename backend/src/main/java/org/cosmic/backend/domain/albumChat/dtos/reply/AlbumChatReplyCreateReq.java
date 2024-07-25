package org.cosmic.backend.domain.albumChat.dtos.reply;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
