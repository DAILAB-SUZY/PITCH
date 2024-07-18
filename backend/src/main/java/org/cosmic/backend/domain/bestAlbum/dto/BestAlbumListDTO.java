package org.cosmic.backend.domain.bestAlbum.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BestAlbumListDTO {
    private Long userId;
    private List<bestAlbumDetail> bestalbum;
}
