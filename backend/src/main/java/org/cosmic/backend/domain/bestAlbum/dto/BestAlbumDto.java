package org.cosmic.backend.domain.bestAlbum.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BestAlbumDto {
    Long userId;
    Long albumId;
}