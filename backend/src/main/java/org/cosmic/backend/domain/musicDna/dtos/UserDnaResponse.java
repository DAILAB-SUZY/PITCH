package org.cosmic.backend.domain.musicDna.dtos;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class UserDnaResponse {
    private Long userId;
    private String emotion;
    public UserDnaResponse(Long userId, String emotion) {
        this.userId = userId;
        this.emotion = emotion;
    }
}
