package org.cosmic.backend.domain.albumChat.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateAlbumChatCommentReq {
    private Long userId;
    private Long albumChatId;
    private String content;
    private Instant createTime;
}
