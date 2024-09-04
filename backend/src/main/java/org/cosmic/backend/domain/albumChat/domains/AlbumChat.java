package org.cosmic.backend.domain.albumChat.domains;
import jakarta.persistence.*;
import lombok.*;
import org.cosmic.backend.domain.playList.domains.Album;

import java.time.Instant;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
    @Table(name = "album_chat") // 테이블 이름 수정
@EqualsAndHashCode(exclude = {"album"})
public class AlbumChat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "albumChat_id") // 컬럼 이름 명시
    private Long albumChatId;

    @Column(nullable=false)
    private String cover;

    @Column(nullable=false)
    private String title;

    @Column(nullable=false)
    private String genre;

    @Column(nullable=false,name="artist_name")
    private String artistName;

    @Builder.Default
    @Column(nullable=false,name="create_time")
    private Instant CreateTime=Instant.now();

    //앨범이랑 연관져야할듯
    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="album_id")
    private Album album;

    @OneToMany(mappedBy = "albumChat")
    private List<AlbumChatComment> albumChatComments;

    @OneToMany(mappedBy = "albumChat")
    private List<AlbumLike> albumLikes;
    @Override
    public String toString() {
        return "AlbumChat{" +
                "albumChatId=" + albumChatId +
                ", cover='" + cover + '\'' +
                ", title='" + title + '\'' +
                ", genre='" + genre + '\'' +
                ", artistName='" + artistName + '\'' +
                ", CreateTime=" + CreateTime +
                ", album=" + (album != null ? album.getAlbumId() : "null") +
                '}';
    }
}