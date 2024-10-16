package org.cosmic.backend.domain.albumChat.dtos.albumChat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.albumChat.domains.AlbumChatComment;
import org.cosmic.backend.domain.albumChat.domains.AlbumLike;
import org.cosmic.backend.domain.albumChat.dtos.albumlike.AlbumChatAlbumLikeDetail;
import org.cosmic.backend.domain.albumChat.dtos.comment.AlbumChatCommentDetail;
import org.cosmic.backend.domain.playList.domains.Album;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
public class AlbumChatDetail {
    private Long albumId;
    private String cover;
    private String genre;
    private String title;
    private String artistName;
    private List<AlbumChatCommentDetail> comments;
    private List<AlbumChatAlbumLikeDetail> albumLike;

    public AlbumChatDetail(Album album) {
        this.albumId = album.getAlbumId();
        this.cover = album.getAlbumCover();
        this.title = album.getTitle();
        this.artistName = album.getArtist().getArtistName();
        this.comments=album.getAlbumChatComments().stream().map(AlbumChatComment::toAlbumChatCommentDetail).toList();
        this.albumLike=album.getAlbumLike().stream().map(AlbumLike::toAlbumChatAlbumLikeDetail).toList();
    }

    public static AlbumChatDetail from(AlbumChatComment albumChatComment) {
        return AlbumChatDetail.builder()
                .albumId(albumChatComment.getAlbum().getAlbumId())
                .cover(albumChatComment.getAlbum().getAlbumCover())
                .genre(albumChatComment.getAlbum().getGenre())
                .title(albumChatComment.getAlbum().getTitle())
                .artistName(albumChatComment.getAlbum().getArtist().getArtistName())
                .comments(albumChatComment.getAlbum().getAlbumChatComments().stream().map(AlbumChatComment::toAlbumChatCommentDetail).toList())
                .albumLike(albumChatComment.getAlbum().getAlbumLike().stream().map(AlbumLike::toAlbumChatAlbumLikeDetail).toList())
                .build();
    }

    public static List<AlbumChatDetail> from(List<AlbumChatComment> albumChatComments) {
        return albumChatComments.stream().map(AlbumChatDetail::from).toList();
    }

}
