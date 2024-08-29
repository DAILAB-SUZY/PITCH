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
public class AlbumChatCommentCreateReq {
    private Long userId;
    private Long albumChatId;
    private String content;
    private Instant createTime;

    public static AlbumChatCommentCreateReq createAlbumChatCommentCreateReq(Long userId, Long albumchatId,String content,Instant createTime) {
        return  AlbumChatCommentCreateReq.builder()
                .userId(userId)
                .albumChatId(albumchatId)
                .content(content)
                .createTime(createTime)
                .build();
    }
}
