package org.cosmic.backend.domain.user.domains;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.albumChat.domains.AlbumChatComment;
import org.cosmic.backend.domain.bestAlbum.domains.UserBestAlbum;
import org.cosmic.backend.domain.favoriteArtist.domains.FavoriteArtist;
import org.cosmic.backend.domain.musicDna.domains.MusicDna;
import org.cosmic.backend.domain.musicDna.dtos.DnaDetail;
import org.cosmic.backend.domain.playList.domains.Playlist;
import org.cosmic.backend.domain.post.entities.Post;
import org.cosmic.backend.domain.post.entities.PostComment;
import org.cosmic.backend.domain.post.entities.PostLike;
import org.cosmic.backend.domain.user.dtos.UserDetail;

@Data
@NoArgsConstructor
@Entity
@AllArgsConstructor
@Builder
@Table(name = "users")  // 테이블 이름이 'user'인 경우
@EqualsAndHashCode(exclude = {"email", "playlist", "posts", "postComments", "postLikes"})
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long userId;

  @OneToOne
  @JoinColumn(name = "email", nullable = false)  // 외래 키 컬럼명은 emails 테이블의 'email' 컬럼과 일치
  private Email email; // FK

  @Column(nullable = false)
  private String username;

  @Column(nullable = false)
  private String password;

  @Builder.Default
  @Column()
  private String profilePicture = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT9wcivPzk14xFzT39uTuyuwulGq92EpDXUfw&s";

  @Builder.Default
  @Column()
  private Instant create_time = Instant.now();

  @ManyToOne
  @JoinColumn(name = "dna1_id")
  private MusicDna dna1;

  @ManyToOne
  @JoinColumn(name = "dna2_id")
  private MusicDna dna2;

  @ManyToOne
  @JoinColumn(name = "dna3_id")
  private MusicDna dna3;

  @ManyToOne
  @JoinColumn(name = "dna4_id")
  private MusicDna dna4;

  @OneToOne(mappedBy = "user")
  private Playlist playlist;

  @OneToOne(mappedBy = "user")
  private FavoriteArtist favoriteArtist;

  @Builder.Default
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Post> posts = new ArrayList<>();

  @Builder.Default
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<PostComment> postComments = new ArrayList<>();

  @Builder.Default
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<PostLike> postLikes = new ArrayList<>();

  @Builder.Default
  @OneToMany(mappedBy = "other", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Follow> followers = new ArrayList<>();

  @Builder.Default
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Follow> followings = new ArrayList<>();

  @Builder.Default
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<UserBestAlbum> bestAlbums = new ArrayList<>();

  @Builder.Default
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<AlbumChatComment> albumChatComments = new ArrayList<>();

  public User(Email email, String username, String password) {
    this.email = email;
    this.username = username;
    this.password = password;
    this.create_time = Instant.now();
  }

  public static UserDetail toUserDetail(User user) {
    return UserDetail.builder()
        .id(user.userId)
        .username(user.username)
        .profilePicture(user.profilePicture)
        .dnas(user.toDnaDetails())
        .build();
  }

  private List<DnaDetail> toDnaDetails() {
    return Stream.of(dna1, dna2, dna3, dna4).map(dna -> dna != null ? DnaDetail.from(dna) : null)
        .toList();
  }

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

  public List<MusicDna> getDNAs() {
    return List.of(dna1, dna2, dna3, dna4);
  }

  public void setDNAs(List<MusicDna> dnaList) {
    setDNAs(dnaList.get(0), dnaList.get(1), dnaList.get(2), dnaList.get(3));
  }

  @PrePersist
  public void prePersist() {
    if (this.create_time == null) {
      this.create_time = Instant.now();  // 저장되기 전에 create_time이 null이면 현재 시간으로 설정
    }
  }
}