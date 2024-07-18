package org.cosmic.backend.domain.bestAlbum.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.playList.domain.Album;
import org.cosmic.backend.domain.user.domain.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name="`Album_User`")
public class Album_User {
    //user와 album은 1:n관계
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="Id")
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="userId")
    private User user;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="AlbumId")
    private Album album;

    public Album_User(Album album, User user) {
        this.album = album;
        this.user = user;
    }
}
