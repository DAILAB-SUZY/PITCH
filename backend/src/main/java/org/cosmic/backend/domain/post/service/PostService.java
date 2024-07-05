package org.cosmic.backend.domain.post.service;

import org.cosmic.backend.domain.post.dto.Album;
import org.cosmic.backend.domain.post.dto.AlbumDto;
import org.cosmic.backend.domain.post.dto.Artist;
import org.cosmic.backend.domain.post.dto.Post;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {

    public List<Post> getAllPosts(Long userId)
    {
        List<Post> posts = new ArrayList<>();
        Post post = new Post();
        post.setUserId(userId);
        post.setCover("base");
        post.setTitle("밤양갱");
        post.setArtistName("비비");
        post.setContent("안녕하세요");
        post.setUpdateTime(Instant.now());
        posts.add(post);
        return posts;
    }

    public Post getPostById(Long userId, Long postId) {
        Post post = new Post();
        //여기서 postId를 찾는 과정 진행
        //찾았다면 해당 post를 가져오고 return
        post.setUserId(userId);
        post.setCover("base");
        post.setTitle("밤양갱");
        post.setArtistName("비비");
        post.setContent("안녕하세요");
        post.setUpdateTime(Instant.now());
        return post;
    }

    public void createPost(Post post) {
        Post newpost = new Post();
        newpost.setUserId(post.getUserId());
        newpost.setCover(post.getCover());
        newpost.setTitle(post.getTitle());
        newpost.setArtistName(post.getArtistName());
        newpost.setContent(post.getContent());
        newpost.setUpdateTime(Instant.now());
        //repository에 넣고
    }

    public void updatePost(Post post) {
        Post newpost = new Post();
        newpost.setUserId(post.getUserId());
        newpost.setCover(post.getCover());
        newpost.setTitle(post.getTitle());
        newpost.setArtistName(post.getArtistName());
        newpost.setContent(post.getContent());
        newpost.setUpdateTime(Instant.now());
        //repository에 넣고
    }

    public void deletePost(Long userId, Long postId) {

        //없으면 지우지 못한다는 에러
        //있다면 repository에서 지움

    }

    public List<AlbumDto> searchAlbum(Album album) {//postid가져오면
        List<AlbumDto> albums = new ArrayList<>();
        AlbumDto albumDto = new AlbumDto();
        albumDto.setAlbumId(1L);
        albumDto.setAlbumName(album.getAlbumName());
        albumDto.setArtistName("아이유");
        albums.add(albumDto);
        return albums;
    }

    public List<AlbumDto> searchArtist(Artist artist) {//postid가져오면
        List<AlbumDto> albums = new ArrayList<>();
        AlbumDto albumDto = new AlbumDto();
        albumDto.setAlbumId(1L);
        albumDto.setAlbumName("Celebrity");
        albumDto.setArtistName(artist.getArtistName());
        albums.add(albumDto);
        return albums;
    }

}
