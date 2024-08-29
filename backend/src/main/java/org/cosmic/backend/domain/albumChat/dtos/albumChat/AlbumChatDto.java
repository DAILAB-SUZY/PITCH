package org.cosmic.backend.domain.albumChat.dtos.albumChat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.albumChat.dtos.commentlike.AlbumChatCommentLikeDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AlbumChatDto {
    private Long albumChatId;

    public static AlbumChatDto createAlbumChatDto(Long albumChatId) {
        return  AlbumChatDto.builder()
                .albumChatId(albumChatId)
                .build();
    }
}
