package org.cosmic.backend.domain.albumChat.dtos.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AlbumChatCommentUpdateReq {
    private Long userId;
    private Long albumChatId;
    private Long albumChatCommentId;
    private String content;
    private Instant createTime;
}
