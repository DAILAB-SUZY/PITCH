package org.cosmic.backend.domain.playList.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class playlistDTO {
    private Long id;
    private List<playlistDetail> playlist;
}
