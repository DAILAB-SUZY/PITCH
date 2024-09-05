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
    private Long albumId;
    private String content;
    private Instant createTime;

    public static AlbumChatCommentUpdateReq createAlbumChatCommentUpdateReq(
        Long userId, Long albumId, String content, Instant createTime) {
        return  AlbumChatCommentUpdateReq.builder()
                .userId(userId)
                .albumId(albumId)
                .content(content)
                .createTime(createTime)
                .build();
    }
}
