package org.cosmic.backend.domain.bestAlbum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlbumGiveDto {
    String title;
    String artistName;
    String cover;
}
