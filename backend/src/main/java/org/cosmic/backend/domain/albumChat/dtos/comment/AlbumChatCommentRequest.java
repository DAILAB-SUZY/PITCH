package org.cosmic.backend.domain.albumChat.dtos.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AlbumChatCommentRequest {
    private String content;
    private Instant createTime;
    private String sorted;
    private int count;
    private Long parentAlbumChatCommentId;

    public static AlbumChatCommentRequest createAlbumChatCommentReq(
         String content, Instant createTime,String sorted,int count,Long parentAlbumChatCommentId) {
        return  AlbumChatCommentRequest.builder()
                .content(content)
                .createTime(createTime)
                .sorted(sorted)
                .count(count)
                .parentAlbumChatCommentId(parentAlbumChatCommentId)
                .build();
    }
}
