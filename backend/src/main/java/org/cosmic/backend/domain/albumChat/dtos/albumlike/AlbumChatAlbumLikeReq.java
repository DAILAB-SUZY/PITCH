package org.cosmic.backend.domain.albumChat.dtos.albumlike;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
public class AlbumChatAlbumLikeReq{
    private Long albumChatAlbumLikeId;

    public AlbumChatAlbumLikeReq(Long albumChatAlbumLikeId) {
        this.albumChatAlbumLikeId = albumChatAlbumLikeId;
    }
}
