package org.cosmic.backend.domain.musicDNA.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDnaResponse {
    private Long userId;
    private String Emotion;
}
