package org.cosmic.backend.domain.albumChat.domains;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
    @JoinColumn(name = "albumChat_id")
    private AlbumChat albumChat;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public AlbumChatAlbumLike(final User user,final AlbumChat albumChat) {
        this.albumChat = albumChat;
        this.user = user;
    }
}
