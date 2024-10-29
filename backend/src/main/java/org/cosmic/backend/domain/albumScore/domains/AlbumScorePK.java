package org.cosmic.backend.domain.albumScore.domains;

import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@EqualsAndHashCode
public class AlbumScorePK implements Serializable {
    private Long user;
    private Long album;
}
