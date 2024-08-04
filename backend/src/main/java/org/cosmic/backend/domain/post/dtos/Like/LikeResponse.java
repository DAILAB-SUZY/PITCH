package org.cosmic.backend.domain.post.dtos.Like;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LikeResponse {
    private Long userId;
    private String userName;
    private String profilePicture;
}
