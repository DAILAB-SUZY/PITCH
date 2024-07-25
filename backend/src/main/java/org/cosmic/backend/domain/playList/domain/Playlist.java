package org.cosmic.backend.domain.playList.domain;

import jakarta.persistence.*;
import lombok.*;
import org.cosmic.backend.domain.user.domains.User;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name="`playlist`")
@EqualsAndHashCode(exclude = {"user", "playlist_track"})
public class Playlist {//트랙은 플레이리스트는 N:M관계임
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="playlistId")
    private Long playlistId ;

    @Builder.Default
    @Column(nullable=false)
    private Instant createdDate=Instant.now();//생성 날짜

    @Builder.Default
    @Column(nullable=false)
    private Instant updatedDate=Instant.now();//최신 업데이트 날짜

    @OneToOne
    @JoinColumn(name="userId")
    private User user;
    //user id로 플레이리스트의 주인을 찾음

    @OneToMany(mappedBy = "playlist")
    @Builder.Default
    private List<Playlist_Track>playlist_track=new ArrayList<>();

    public Playlist(Instant createdDate, Instant updatedDate, User user){
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.user = user;
    }
}