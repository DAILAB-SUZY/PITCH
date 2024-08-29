package org.cosmic.backend.domain.user.domains;

import jakarta.persistence.*;
import lombok.*;
import org.cosmic.backend.domain.favoriteArtist.domains.FavoriteArtist;
import org.cosmic.backend.domain.musicDna.domains.User_Dna;
import org.cosmic.backend.domain.playList.domains.Playlist;
import org.cosmic.backend.domain.post.entities.Comment;
import org.cosmic.backend.domain.post.entities.Like;
import org.cosmic.backend.domain.post.entities.Post;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@AllArgsConstructor
@Builder
@Table(name="users")  // 테이블 이름이 'user'인 경우
@EqualsAndHashCode(exclude = {"email", "playlist", "userDnas", "posts", "comments", "likes"})
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long userId;

    @OneToOne
    @JoinColumn(name="email")  // 외래 키 컬럼명은 emails 테이블의 'email' 컬럼과 일치
    private Email email; // FK

    @Column(nullable=false)
    private String username;

    @Column(nullable=false)
    private String password;

    @Builder.Default
    @Column()
    private String profilePicture="base";

    @Builder.Default
    @Column(nullable=false)
    private Instant signupDate=Instant.now();

    @OneToMany(mappedBy = "user")
    @Builder.Default
    private List<User_Dna>userDnas=new ArrayList<>();

    @OneToOne(mappedBy = "user")
    private Playlist playlist;

    @OneToOne(mappedBy = "user")
    private FavoriteArtist favoriteAlbum;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Post> posts=new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Comment> comments=new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Like> likes=new ArrayList<>();

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", email=" + (email != null ? email.getEmail() : "null") +
                ", username='" + username + '\'' +
                ", profilePicture='" + profilePicture + '\'' +
                ", signupDate=" + signupDate +
                '}';
    }
    public User(Email email, String username, String password){
        this.email=email;
        this.username=username;
        this.password=password;
    }
}