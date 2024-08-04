package org.cosmic.backend.domain.post.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.user.domains.User;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comment") // 테이블 이름 수정
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id") // 컬럼 이름 명시
    private Long commentId;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    private String content;
    private Instant updateTime;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reply> replies;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
