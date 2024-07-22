package org.cosmic.backend.domain.albumChat.dtos.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlbumChatCommentResponse {
    private Long userId;
    private Long albumChatCommentId;
    private String content;
    private Instant createTime;
}
