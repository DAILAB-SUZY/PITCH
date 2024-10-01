package org.cosmic.backend.domain.favoriteArtist.domains;

import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@EqualsAndHashCode
public class FavoriteArtistPK implements Serializable {
    private Long artist;
    private Long user;
}
