package org.cosmic.backend.domain.albumChat.domains;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.playList.domains.Album;
import org.cosmic.backend.domain.user.domains.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="`albumChatAlbumLike`")
public class AlbumChatAlbumLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long albumChatAlbumLikeId;

    @ManyToOne
    @JoinColumn(name = "album_id")
    private Album album;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public AlbumChatAlbumLike(final User user,final Album album) {
        this.album = album;
        this.user = user;
    }
}
