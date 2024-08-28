package org.cosmic.backend.domain.post.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.post.dtos.Post.PostDto;
import org.cosmic.backend.domain.post.dtos.Post.PostReq;
import org.cosmic.backend.domain.user.domains.User;

import java.time.Instant;
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

    private String cover; // 앨범커버
    private String title; // 앨범제목
    private String content; // 내용
    private String artistName;
    private Instant updateTime;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Like> likes;

    public static PostReq toPostReq(Post post) {
        return PostReq.builder()
                .postId(post.getPostId())
                .cover(post.getCover())
                .title(post.getTitle())
                .content(post.getContent())
                .artistName(post.getArtistName())
                .updateTime(post.getUpdateTime())
                .build();
    }

    public static PostDto toPostDto(Post post) {
        return PostDto.builder()
                .postId(post.getPostId())
                .build();
    }
}
