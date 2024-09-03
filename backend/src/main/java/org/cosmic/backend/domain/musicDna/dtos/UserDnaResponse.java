package org.cosmic.backend.domain.musicDna.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.musicDna.domains.User_Dna;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDnaResponse {
    private Long userId;
    private String emotion;

    public UserDnaResponse(User_Dna userDna) {
        this.userId = userDna.getUser().getUserId();
        this.emotion = userDna.getEmotion().getName();
    }
}
