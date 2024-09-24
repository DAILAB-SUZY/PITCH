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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSSX", timezone = "UTC")
    private Instant createTime;

    public static AlbumChatCommentRequest createAlbumChatCommentReq(
         String content, Instant createTime) {
        return  AlbumChatCommentRequest.builder()
                .content(content)
                .createTime(createTime)
                .build();
    }
}
