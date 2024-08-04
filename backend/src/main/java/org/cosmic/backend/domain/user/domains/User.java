package org.cosmic.backend.domain.user.domains;

import jakarta.persistence.*;
import lombok.*;
import org.cosmic.backend.domain.favoriteArtist.domains.FavoriteArtist;
import org.cosmic.backend.domain.musicDna.domains.User_Dna;
import org.cosmic.backend.domain.post.entities.Comment;
import org.cosmic.backend.domain.post.entities.Like;
import org.cosmic.backend.domain.post.entities.Post;
import org.cosmic.backend.domain.playList.domains.Playlist;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@AllArgsConstructor
@Builder
@Table(name="`user`")
@EqualsAndHashCode(exclude = {"email", "playlist", "userDnas", "posts", "comments", "likes"})
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long userId;

    @OneToOne
    @JoinColumn(name="emails")
    private Email email;//fk

    @Column(nullable=false,length=255)
    private String username;

    @Column(nullable=false,length=255)
    private String password;

    @Builder.Default
    @Column(length=255)
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