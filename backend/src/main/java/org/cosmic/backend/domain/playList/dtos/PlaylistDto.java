package org.cosmic.backend.domain.playList.dtos;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
public class PlaylistDto {
    private List<PlaylistDetail> playlist;
    public PlaylistDto(final List<PlaylistDetail> playlist) {
        this.playlist = playlist;
    }
}
