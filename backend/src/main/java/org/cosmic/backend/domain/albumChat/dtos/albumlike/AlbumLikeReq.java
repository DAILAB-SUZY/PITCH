package org.cosmic.backend.domain.albumChat.dtos.albumlike;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlbumLikeReq {
    private Long albumId;
    private Long userId;

    public static AlbumLikeReq createAlbumChatAlbumLikeReq(Long albumId, Long userId){
        return  AlbumLikeReq.builder()
                .albumId(albumId)
                .userId(userId)
                .build();
    }
}
