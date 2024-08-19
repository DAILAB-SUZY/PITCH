package org.cosmic.backend.domain.bestAlbum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BestAlbumGiveDto {
    Long albumId;
    String albumName;
    String cover;
}