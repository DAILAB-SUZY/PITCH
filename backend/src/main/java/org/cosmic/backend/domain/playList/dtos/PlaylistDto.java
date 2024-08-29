package org.cosmic.backend.domain.playList.dtos;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
public class PlaylistDto {
    private Long id;
    private List<PlaylistDetail> playlist;
    public PlaylistDto(Long userId,final List<PlaylistDetail> playlist) {
        this.id = userId;
        this.playlist = playlist;
    }
}
