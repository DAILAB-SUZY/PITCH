package org.cosmic.backend.domain.bestAlbum.domains;

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
@Table(name="user_bestalbum")
@IdClass(UserBestAlbumPK.class)
public class UserBestAlbum {

    @Id
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="userId")
    private User user;

    @Id
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="AlbumId")
    private Album album;

    @Builder.Default
    @Column(name="`order`")
    private Integer order = 0;
}
