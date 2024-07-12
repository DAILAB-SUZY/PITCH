package org.cosmic.backend.domain.post.entity;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.user.domain.User;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="`reply`")
public class Reply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long replyId;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;

    private String content;
    private Instant updateTime;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
