package org.cosmic.backend.domain.user.domains;

import jakarta.persistence.*;
import lombok.*;
import org.cosmic.backend.domain.favoriteArtist.domains.FavoriteArtist;
import org.cosmic.backend.domain.musicDna.domains.MusicDna;
import org.cosmic.backend.domain.playList.domains.Playlist;
import org.cosmic.backend.domain.post.entities.PostComment;
import org.cosmic.backend.domain.post.entities.PostLike;
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
@EqualsAndHashCode(exclude = {"email", "playlist", "posts", "postComments", "postLikes"})
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
    private Instant create_time =Instant.now();

    @ManyToOne
    @JoinColumn(name="dna1_id")
    private MusicDna dna1;

    @ManyToOne
    @JoinColumn(name="dna2_id")
    private MusicDna dna2;

    @ManyToOne
    @JoinColumn(name="dna3_id")
    private MusicDna dna3;

    @ManyToOne
    @JoinColumn(name="dna4_id")
    private MusicDna dna4;

    @OneToOne(mappedBy = "user")
    private Playlist playlist;

    @OneToOne(mappedBy = "user")
    private FavoriteArtist favoriteAlbum;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Post> posts=new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PostComment> postComments =new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PostLike> postLikes =new ArrayList<>();

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", email=" + (email != null ? email.getEmail() : "null") +
                ", username='" + username + '\'' +
                ", profilePicture='" + profilePicture + '\'' +
                ", signupDate=" + create_time +
                '}';
    }

    public void setDNAs(MusicDna dna1, MusicDna dna2, MusicDna dna3, MusicDna dna4) {
        this.dna1 = dna1;
        this.dna2 = dna2;
        this.dna3 = dna3;
        this.dna4 = dna4;
    }

    public void setDNAs(List<MusicDna> dnaList) {
        setDNAs(dnaList.get(0), dnaList.get(1), dnaList.get(2), dnaList.get(3));
    }

    public List<MusicDna> getDNAs() {
        List<MusicDna> dnaList = new ArrayList<>();
        dnaList.add(dna1);
        dnaList.add(dna2);
        dnaList.add(dna3);
        dnaList.add(dna4);
        return dnaList;
    }

    public User(Email email, String username, String password){
        this.email=email;
        this.username=username;
        this.password=password;
    }
}