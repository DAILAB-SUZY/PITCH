package org.cosmic.backend.domain.albumChat.dtos.reply;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
public class AlbumChatReplyDto {
    private Long albumChatReplyId;
    public AlbumChatReplyDto(final Long albumChatReplyId) {
        this.albumChatReplyId = albumChatReplyId;
    }

    public static AlbumChatReplyDto createAlbumChatReplyDto(Long albumChatReplyId) {
        return  AlbumChatReplyDto.builder()
                .albumChatReplyId(albumChatReplyId)
                .build();
    }
}
