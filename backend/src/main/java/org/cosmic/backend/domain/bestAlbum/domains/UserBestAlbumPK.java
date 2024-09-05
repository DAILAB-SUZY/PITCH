package org.cosmic.backend.domain.bestAlbum.domains;

import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@EqualsAndHashCode
public class UserBestAlbumPK implements Serializable {
    private Long user;
    private Long album;
}
