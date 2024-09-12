package org.cosmic.backend.domain.albumChat.domains;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.playList.domains.Album;
import org.cosmic.backend.domain.user.domains.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name="`album_like`")
@IdClass(AlbumLikePK.class)
public class AlbumLike {

    @Id
    @ManyToOne
    @JoinColumn(name = "album_id")
    private Album album;

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
