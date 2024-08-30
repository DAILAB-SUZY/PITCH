package org.cosmic.backend.domain.albumChat.dtos.albumlike;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AlbumChatAlbumLikeDto {
    private Long userId;
    private Long albumChatId;

    public static AlbumChatAlbumLikeDto createAlbumChatAlbumLikeDto(Long userId, Long albumchatId){
        return  AlbumChatAlbumLikeDto.builder()
                .userId(userId)
                .albumChatId(albumchatId)
                .build();
    }
}
