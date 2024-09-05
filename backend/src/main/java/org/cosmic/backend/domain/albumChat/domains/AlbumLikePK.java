package org.cosmic.backend.domain.albumChat.domains;

import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@EqualsAndHashCode
public class AlbumLikePK implements Serializable {
    private Long user;
    private Long album;
}
