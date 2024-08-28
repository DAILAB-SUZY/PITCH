package org.cosmic.backend.domain.post.applications;

import org.cosmic.backend.domain.playList.domains.Album;
import org.cosmic.backend.domain.playList.exceptions.NotFoundArtistException;
import org.cosmic.backend.domain.playList.exceptions.NotFoundUserException;
import org.cosmic.backend.domain.playList.exceptions.NotMatchAlbumException;
import org.cosmic.backend.domain.playList.repositorys.AlbumRepository;
import org.cosmic.backend.domain.playList.repositorys.ArtistRepository;
import org.cosmic.backend.domain.post.dtos.Post.*;
import org.cosmic.backend.domain.post.entities.Post;
import org.cosmic.backend.domain.post.exceptions.NotFoundAlbumException;
import org.cosmic.backend.domain.post.exceptions.NotFoundPostException;
import org.cosmic.backend.domain.post.repositories.PostRepository;
import org.cosmic.backend.domain.user.repositorys.UsersRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final UsersRepository userRepository;
    private final ArtistRepository artistRepository;
    private final AlbumRepository albumRepository;

    public PostService(PostRepository postRepository, UsersRepository userRepository, ArtistRepository artistRepository, AlbumRepository albumRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.artistRepository = artistRepository;
        this.albumRepository = albumRepository;
    }

    public List<PostReq> getAllPosts(Long userId) { // 해당 유저의 post들을 모두 가져오는
        if(userRepository.findById(userId).isEmpty()) {
            throw new NotFoundUserException();
        }
        return postRepository.findByUser_UserId(userId)
                .stream()
                .map(Post::toPostReq)
                .toList();
    }

    public PostReq getPostById(Long postId) {
        //해당 postId인 포스트만 가져온다.
        if(postRepository.findById(postId).isEmpty()) {
            throw new NotFoundPostException();
        }
        return Post.toPostReq(postRepository.findById(postId).get());
    }

    public PostDto createPost(CreatePost postInfo) {
        //post생성 버튼 눌렀을 때
        if(userRepository.findById(postInfo.getUserId()).isEmpty()) {
            throw new NotFoundUserException();
        }
        if(albumRepository.findByTitleAndArtist_ArtistName(postInfo.getTitle(),postInfo.getArtistName()).isEmpty()){
            throw new NotMatchAlbumException();
        }
        return Post.toPostDto(postRepository.save(Post.builder()
                .title(postInfo.getTitle())
                .artistName(postInfo.getArtistName())
                .content(postInfo.getContent())
                .updateTime(Instant.now())
                .user(userRepository.findById(postInfo.getUserId()).get())
                .cover(postInfo.getCover())
                .build()));
    }

    public void updatePost(UpdatePost postInfo) {
        if(postRepository.findById(postInfo.getPostId()).isEmpty()) {
            throw new NotFoundPostException();
        }
        Post updatedPost = postRepository.findByPostId(postInfo.getPostId());
        updatedPost.setContent(postInfo.getContent());
        updatedPost.setUpdateTime(Instant.now());
        postRepository.save(updatedPost);//새로생기는지 업데이트만 되는지 만약 새로생기는거면업데이트만 되게만들어야함.
     }

    public void deletePost(Long postId) {
        if(postRepository.findById(postId).isEmpty()) {
            throw new NotFoundPostException();
        }
        postRepository.deleteById(postId);
    }
//*
    public List<AlbumDto> searchAlbum(String albumName) {
        //사용자가 앨범 찾기 위해 앨범 이름을 검색할 때
        if(albumRepository.findAllByTitle(albumName).isEmpty()) {
            throw new NotFoundAlbumException();
        }
        return albumRepository.findAllByTitle(albumName)
                .stream()
                .map(Album::toAlbumDto)
                .toList();
    }

    public List<AlbumDto> searchArtist(String artistName) {
        //사용자가 앨범 찾기 위해 아티스트 이름을 검색할 때
        if(artistRepository.findByArtistName(artistName).isEmpty()) {
            throw new NotFoundArtistException();
        }
        return albumRepository.findAllByArtist_ArtistName(artistName)
                .stream()
                .map(Album::toAlbumDto)
                .toList();

    }

}
