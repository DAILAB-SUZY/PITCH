package org.cosmic.backend.domain.albumChat.dtos.albumChat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cosmic.backend.domain.albumChat.domains.AlbumChatComment;
import org.cosmic.backend.domain.albumChat.domains.AlbumLike;
import org.cosmic.backend.domain.albumChat.dtos.albumlike.AlbumChatAlbumLikeDetail;
import org.cosmic.backend.domain.albumChat.dtos.comment.AlbumChatCommentDetail;

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
