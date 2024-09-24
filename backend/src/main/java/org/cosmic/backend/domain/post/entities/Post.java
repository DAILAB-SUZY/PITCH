package org.cosmic.backend.domain.post.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.playList.domains.Album;
import org.cosmic.backend.domain.post.dtos.Post.PostDetail;
import org.cosmic.backend.domain.post.dtos.Post.PostDto;
import org.cosmic.backend.domain.post.dtos.Post.PostReq;
import org.cosmic.backend.domain.user.domains.User;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "post") // 테이블 이름 수정
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id") // 컬럼 이름 명시
    private Long postId;

    private String content; // 내용
    private Instant update_time;
    @Builder.Default
    private Instant create_time=Instant.now();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "album_id")
    private Album album;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Builder.Default
    private List<PostComment> postComments = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PostLike> postLikes = new ArrayList<>();

    public static PostDetail toPostDetail(Post post) {
        return PostDetail.builder()
                .postId(post.postId)
                .content(post.content)
                .createAt(post.create_time)
                .updateAt(post.update_time)
                .album(Album.toAlbumDetail(post.album))
                .author(User.toUserDetail(post.user))
                .comments(post.postComments.stream().map(PostComment::toCommentDetail).toList())
                .build();
    }

    public static PostReq toPostReq(Post post) {
        return PostReq.builder()
                .postId(post.getPostId())
                .content(post.getContent())
                .update_time(post.getUpdate_time())
                .build();
    }

    public static PostDto toPostDto(Post post) {
        return PostDto.builder()
                .postId(post.getPostId())
                .build();
    }
}
