package org.cosmic.backend.domain.playList.domains;

import jakarta.persistence.*;
import lombok.*;
import org.cosmic.backend.domain.albumChat.domains.AlbumChatComment;
import org.cosmic.backend.domain.albumChat.domains.AlbumChatCommentLike;
import org.cosmic.backend.domain.albumChat.domains.AlbumLike;
import org.cosmic.backend.domain.albumChat.dtos.albumChat.AlbumChatDetail;
import org.cosmic.backend.domain.post.dtos.Post.AlbumDetail;
import org.cosmic.backend.domain.post.dtos.Post.AlbumDto;
import org.cosmic.backend.domain.post.entities.Post;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name="Album")
@EqualsAndHashCode
public class Album {//앨범과 트랙은 1:N관계이며 앨범과 아티스트는 더 생각 필요

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="album_id")
    private Long albumId;

    @Column(nullable=false)
    private String title;//앨범 제목

    @Column(nullable=false)
    private String cover;

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Genre> genre;

    @Builder.Default
    @Column(nullable=false)
    private Instant createdDate=Instant.now();//발매 일

    @OneToMany(mappedBy = "album")
    @Builder.Default
    private Set<Post> posts = new HashSet<>();

    //아티스트와 1:N 관계
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="artist_id")
    private Artist artist;

    @OneToMany(mappedBy = "album")
    @Builder.Default
    private List<Track> tracks=new ArrayList<>();

    @OneToMany(mappedBy = "album")
    private List<AlbumChatComment> albumChatComments;

    @OneToMany(mappedBy = "album")
    @Builder.Default
    private List<AlbumLike>albumLike=new ArrayList<>();

    public static AlbumDetail toAlbumDetail(Album album) {
        return AlbumDetail.builder()
                .id(album.albumId)
                .title(album.title)
                .cover(album.cover)
                .genre(album.genre.toString())
                .build();
    }
    public static AlbumChatDetail toAlbumChatDetail(Album album) {
        return AlbumChatDetail.builder()
                .albumId(album.albumId)
                .title(album.title)
                .cover(album.cover)
                .genre(album.genre.toString())
                .artistName(album.getArtist().getArtistName())
                .comments(album.albumChatComments.stream().map(AlbumChatComment::toAlbumChatCommentDetail).toList())
                .albumLike(album.albumLike.stream().map(AlbumLike::toAlbumChatAlbumLikeDetail).toList())
                .build();
    }

    @Override
    public String toString() {
        return "Album{" +
                "albumId=" + albumId +
                ", title='" + title + '\'' +
                ", cover='" + cover + '\'' +
                ", genre='" + genre + '\'' +
                ", createdDate=" + createdDate +
                ", artist=" + (artist != null ? artist.getArtistId() : "null") +
                '}';
    }

    public static AlbumDto toAlbumDto(Album album) {
        return AlbumDto.builder()
                .albumId(album.getAlbumId())
                .albumName(album.getTitle())
                .artistName(album.getArtist().getArtistName())
                .build();
    }
}