package org.cosmic.backend.domain.playList.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrackGiveDto {
    String title;
    String artistName;
    String cover;
}
