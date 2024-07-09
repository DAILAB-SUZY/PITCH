package org.cosmic.backend.domain.post.service;

import org.cosmic.backend.domain.playList.domain.Album;
import org.cosmic.backend.domain.playList.domain.Artist;
import org.cosmic.backend.domain.playList.exceptions.NotFoundArtistException;
import org.cosmic.backend.domain.playList.exceptions.NotFoundUserException;
import org.cosmic.backend.domain.playList.repository.AlbumRepository;
import org.cosmic.backend.domain.playList.repository.ArtistRepository;
import org.cosmic.backend.domain.post.dto.Post.*;

import org.cosmic.backend.domain.post.entity.Post;
import org.cosmic.backend.domain.post.exception.NotFoundAlbumException;
import org.cosmic.backend.domain.post.exception.NotFoundPostException;
import org.cosmic.backend.domain.post.repository.PostRepository;
import org.cosmic.backend.domain.user.domain.User;
import org.cosmic.backend.domain.user.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UsersRepository userRepository;
    @Autowired
    private ArtistRepository artistRepository;
    @Autowired
    private AlbumRepository albumRepository;

    public List<PostReq> getAllPosts(Long userId)
    { // 해당 유저의 post들을 모두 가져오는
        List<PostReq> posts = new ArrayList<>();
        if(!userRepository.findById(userId).isPresent())
        {
            throw new NotFoundUserException();
        }
        else{
            List<Post> postList = postRepository.findByUser_UserId(userId);
            for (int i = 0; i < postList.size(); i++) {
                PostReq post = new PostReq(); // 각 iteration마다 새로운 객체 생성
                post.setPostId(postList.get(i).getPostId());
                post.setCover(postList.get(i).getCover());
                post.setTitle(postList.get(i).getTitle());
                post.setArtistName(postList.get(i).getArtistName());
                post.setContent(postList.get(i).getContent());
                post.setUpdateTime(postList.get(i).getUpdateTime());
                posts.add(post);
            }
            return posts;
        }
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
