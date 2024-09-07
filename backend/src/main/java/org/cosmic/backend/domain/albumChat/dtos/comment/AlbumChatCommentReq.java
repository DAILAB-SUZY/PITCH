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
public class AlbumChatCommentReq {
    private String content;
    private Instant createTime;

    public static AlbumChatCommentReq createAlbumChatCommentReq(
         String content, Instant createTime) {
        return  AlbumChatCommentReq.builder()
                .content(content)
                .createTime(createTime)
                .build();
    }
}
