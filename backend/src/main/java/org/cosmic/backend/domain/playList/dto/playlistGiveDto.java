package org.cosmic.backend.domain.playList.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class playlistGiveDto {

    Long playlistId;
    private Instant createdDate;
    private Instant updatedDate;
    Long userId;
}
