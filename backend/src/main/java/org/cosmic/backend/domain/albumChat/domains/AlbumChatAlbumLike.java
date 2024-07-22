package org.cosmic.backend.domain.albumChat.domains;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.user.domain.User;

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
}
