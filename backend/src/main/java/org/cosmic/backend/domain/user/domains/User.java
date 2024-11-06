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
import jakarta.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.albumChat.domains.AlbumChatComment;
import org.cosmic.backend.domain.albumScore.domains.AlbumScore;
import org.cosmic.backend.domain.bestAlbum.domains.UserBestAlbum;
import org.cosmic.backend.domain.favoriteArtist.domains.FavoriteArtist;
import org.cosmic.backend.domain.musicDna.domains.MusicDna;
import org.cosmic.backend.domain.musicDna.dtos.DnaDetail;
import org.cosmic.backend.domain.playList.domains.Album;
import org.cosmic.backend.domain.playList.domains.Playlist;
import org.cosmic.backend.domain.post.entities.Post;
import org.cosmic.backend.domain.post.entities.PostComment;
import org.cosmic.backend.domain.post.entities.PostLike;
import org.cosmic.backend.domain.user.dtos.UserDetail;
import org.springframework.security.core.GrantedAuthority;

@Data
@NoArgsConstructor
@Entity
@AllArgsConstructor
@Builder
@Table(name = "users")  // 테이블 이름이 'user'인 경우
@EqualsAndHashCode(exclude = {"email", "playlist", "posts", "postComments", "postLikes",
    "favoriteArtist"})
public class User implements MyUserDetails {

  private static final String NONE_ARTIST = "";
  private static final String BASE_PROFILE_PICTURE = "basic_profile.webp";

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
  private String profilePicture = BASE_PROFILE_PICTURE;

  @Builder.Default
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

  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
  private Playlist playlist;

  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
  private FavoriteArtist favoriteArtist;

  @Builder.Default
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<AlbumScore> albumScore = new ArrayList<>();

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
        .id(user.getUserId())
        .username(user.getUsername())
        .profilePicture(user.getOriginProfilePicture())
        .dnas(user.toDnaDetails(user))
        .build();
  }

  public static User from(@NotBlank(message = "Name cannot be blank") String name, String encode,
      Email email) {
    email.checkVerified();
    return User.builder()
        .username(name)
        .password(encode)
        .email(email)
        .build();
  }

  public String getOriginProfilePicture() {
    return String.format("%s/api/file/%s", ServerProperty.getServerOrigin(), getProfilePicture());
  }

  private List<DnaDetail> toDnaDetails(User user) {
    return user.getDNAs().stream()
        .map(DnaDetail::from)
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
    return Stream.of(dna1, dna2, dna3, dna4).filter(Objects::nonNull).toList();
  }

  public void setDNAs(List<MusicDna> dnaList) {
    setDNAs(dnaList.get(0), dnaList.get(1), dnaList.get(2), dnaList.get(3));
  }

  public void checkPassword(String password) {
    if (!getPassword().equals(password)) {
      throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
    }
  }

  @PrePersist
  public void prePersist() {
    if (getPlaylist() == null) {
      setPlaylist(Playlist.from(this));
    }
    if (getFavoriteArtist() == null) {
      setFavoriteArtist(FavoriteArtist.from(this));
    }
  }

  public boolean isMe(Long userId) {
    return userId.equals(getUserId());
  }

  public String getFavoriteArtistId() {
    if (getFavoriteArtist().isNotSet()) {
      return NONE_ARTIST;
    }
    return getFavoriteArtist().getSpotifyArtistId();
  }

  public List<String> getPlaylistTracksIds() {
    return getPlaylist().getSpotifyTrackIds();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of();
  }

  @Override
  public boolean isAccountNonExpired() {
    return email.getVerified();
  }

  @Override
  public boolean isAccountNonLocked() {
    return email.getVerified();
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return email.getVerified();
  }

  @Override
  public boolean isEnabled() {
    return email.getVerified();
  }

  @Override
  public String getId() {
    return String.valueOf(userId);
  }

  public AlbumScore getAlbumScore(Album album) {
    return getAlbumScore().stream()
        .filter(albumScore -> albumScore.getAlbum().equals(album))
        .findFirst()
        .orElseGet(() -> {
          AlbumScore albumscore = AlbumScore.NONE_SCORE;
          albumscore.setAlbum(album);
          return albumscore;
        });
  }
}