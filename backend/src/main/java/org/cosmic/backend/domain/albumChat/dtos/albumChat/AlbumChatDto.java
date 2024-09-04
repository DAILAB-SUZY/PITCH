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
    private Long albumId;

    public static AlbumChatDto createAlbumChatDto(Long albumId) {
        return  AlbumChatDto.builder()
                .albumId(albumId)
                .build();
    }
}
