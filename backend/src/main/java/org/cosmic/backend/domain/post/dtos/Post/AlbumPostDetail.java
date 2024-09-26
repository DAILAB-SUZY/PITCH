package org.cosmic.backend.domain.post.dtos.Post;

import org.cosmic.backend.domain.favoriteArtist.dtos.ArtistDetail;
import org.cosmic.backend.domain.user.dtos.AuthorDetail;

import java.time.Instant;

public class AlbumPostDetail {
    private AlbumDetail album;
    private ArtistDetail artist;
    private AuthorDetail author;
    private Instant postCreateTime;
    private Instant postUpdateTime;
    private String content;
}
