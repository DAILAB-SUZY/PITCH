package org.cosmic.backend.domain.playList.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlaylistGiveDto {

    Long playlistId;
    Long trackId;
    Long userId;
    String title;
    String artistName;
    String cover;
}
