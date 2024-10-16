package org.cosmic.backend.domain.albumChat.domains;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.albumChat.dtos.albumChat.AlbumCommentDetail;
import org.cosmic.backend.domain.albumChat.dtos.comment.AlbumChatCommentDetail;
import org.cosmic.backend.domain.playList.domains.Album;
import org.cosmic.backend.domain.post.dtos.Post.AlbumDetail;
import org.cosmic.backend.domain.user.domains.User;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "albumChatComment") // 테이블 이름 수정
public class AlbumChatComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "albumChatComment_id") // 컬럼 이름 명시
    private Long albumChatCommentId;

    @ManyToOne
    @JoinColumn(name = "album_id")
    private Album album;

    private String content;

    @Column(name = "parent_albumChatCommentId")
    private Long parentAlbumChatCommentId;

    @Column(name = "create_time")
    private Instant createTime;

    @Column(name = "update_time")
    private Instant updateTime;

    @OneToMany(mappedBy = "albumChatComment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AlbumChatCommentLike> albumChatCommentLikes=new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public AlbumChatComment(String content,Instant createTime, Instant updateTime, User user,Album album,Long parentAlbumChatCommentId) {
        this.album = album;
        this.content = content;
        this.createTime=createTime;
        this.updateTime = updateTime;
        this.user=user;
        this.parentAlbumChatCommentId=parentAlbumChatCommentId;
    }

    public static AlbumChatCommentDetail toAlbumChatCommentDetail(AlbumChatComment albumChatComment) {
        return AlbumChatCommentDetail.builder()
                .albumChatCommentId(albumChatComment.getAlbumChatCommentId())
                .author(User.toUserDetail(albumChatComment.user))
                .content(albumChatComment.getContent())
                .likes(albumChatComment.albumChatCommentLikes.stream()
                    .map(AlbumChatCommentLike::toAlbumChatCommentLikeDetail).toList())
                .createAt(albumChatComment.createTime)
                .updateAt(albumChatComment.updateTime)
                .build();
    }
    public static List<AlbumCommentDetail> toUserAlbumChatDetail(List<AlbumChatComment> albumChatComments) {
        List<AlbumCommentDetail> albumCommentDetails = new ArrayList<>();
        for(int i=0;i<albumChatComments.size();i++) {
            AlbumCommentDetail albumCommentDetail=new AlbumCommentDetail();
            AlbumChatComment albumChatComment = albumChatComments.get(i);
            albumCommentDetail.setAlbumDetail(AlbumDetail.from(albumChatComment.getAlbum()));
            albumCommentDetail.setAlbumChatCommentDetail(new AlbumChatCommentDetail(albumChatComment));
            albumCommentDetails.add(albumCommentDetail);
        }
        return albumCommentDetails;
    }
    @Override
    public String toString() {
        return "AlbumChatComment{" +
                "albumChatCommentId=" + albumChatCommentId +
                ", content='" + content + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", user=" + (user != null ? user.getUserId() : "null") +
                '}';
    }
}
