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
@Table(name = "albumChat") // 테이블 이름 수정
@EqualsAndHashCode(exclude = {"album"})
public class AlbumChat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "albumChat_id") // 컬럼 이름 명시
    private Long albumChatId;

    private String cover;
    private String title;
    private String genre;
    private String artistName;
    private Instant CreateTime;

    //앨범이랑 연관져야할듯
    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="albumId")
    private Album album;

    @OneToMany(mappedBy = "albumChat")
    private List<AlbumChatComment> albumChatComments;

    @OneToMany(mappedBy = "albumChat")
    private List<AlbumChatAlbumLike> albumChatAlbumLikes;
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