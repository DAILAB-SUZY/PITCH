package org.cosmic.backend.domain.albumChat.dtos.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.albumChat.domains.AlbumChatComment;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlbumChatCommentDetail implements Serializable {

    private Long userId;
    private Long albumChatCommentId;
    private String content;
    private Long createTime;

    public AlbumChatCommentDetail(AlbumChatComment albumChatComment) {
        this.userId = albumChatComment.getUser().getUserId();
        this.albumChatCommentId = albumChatComment.getAlbumChatCommentId();
        this.content = albumChatComment.getContent();
        this.createTime = albumChatComment.getUpdateTime().toEpochMilli();
    }
}
