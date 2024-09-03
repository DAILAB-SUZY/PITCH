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

/**
 * 게시물(Post) 관련 비즈니스 로직을 처리하는 서비스 클래스입니다.
 * 게시물 조회, 생성, 수정, 삭제 및 앨범/아티스트 검색 기능을 제공합니다.
 */
@Service
public class PostService {
    private final PostRepository postRepository;
    private final UsersRepository userRepository;
    private final ArtistRepository artistRepository;
    private final AlbumRepository albumRepository;

    /**
     * PostService의 생성자입니다.
     *
     * @param postRepository 게시물 데이터를 처리하는 리포지토리
     * @param userRepository 사용자 데이터를 처리하는 리포지토리
     * @param artistRepository 아티스트 데이터를 처리하는 리포지토리
     * @param albumRepository 앨범 데이터를 처리하는 리포지토리
     */
    public PostService(PostRepository postRepository, UsersRepository userRepository, ArtistRepository artistRepository, AlbumRepository albumRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.artistRepository = artistRepository;
        this.albumRepository = albumRepository;
    }

    /**
     * 특정 사용자의 모든 게시물을 조회합니다.
     *
     * @param userId 게시물을 조회할 사용자의 ID
     * @return 사용자의 모든 게시물을 포함한 요청 객체 리스트
     *
     * @throws NotFoundUserException 사용자를 찾을 수 없을 때 발생합니다.
     */
    public List<PostReq> getAllPosts(Long userId) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new NotFoundUserException();
        }
        return postRepository.findByUser_UserId(userId)
                .stream()
                .map(Post::toPostReq)
                .toList();
    }

    /**
     * 특정 게시물 ID로 게시물을 조회합니다.
     *
     * @param postId 조회할 게시물의 ID
     * @return 조회된 게시물의 요청 객체
     *
     * @throws NotFoundPostException 게시물을 찾을 수 없을 때 발생합니다.
     */
    public PostReq getPostById(Long postId) {
        if (postRepository.findById(postId).isEmpty()) {
            throw new NotFoundPostException();
        }
        return Post.toPostReq(postRepository.findById(postId).get());
    }

    /**
     * 새로운 게시물을 생성합니다.
     *
     * @param postInfo 생성할 게시물의 정보를 포함한 객체
     * @return 생성된 게시물의 DTO 객체
     *
     * @throws NotFoundUserException 사용자를 찾을 수 없을 때 발생합니다.
     * @throws NotMatchAlbumException 게시물에 해당하는 앨범을 찾을 수 없을 때 발생합니다.
     */
    public PostDto createPost(CreatePost postInfo) {
        if (userRepository.findById(postInfo.getUserId()).isEmpty()) {
            throw new NotFoundUserException();
        }
        if (albumRepository.findByTitleAndArtist_ArtistName(postInfo.getTitle(), postInfo.getArtistName()).isEmpty()) {
            throw new NotMatchAlbumException();
        }
        return Post.toPostDto(postRepository.save(Post.builder()
                .content(postInfo.getContent())
                .update_time(Instant.now())
                .user(userRepository.findById(postInfo.getUserId()).get())
                .build()));
    }

    /**
     * 기존 게시물을 수정합니다.
     *
     * @param postInfo 수정할 게시물의 정보를 포함한 객체
     *
     * @throws NotFoundPostException 게시물을 찾을 수 없을 때 발생합니다.
     */
    public void updatePost(UpdatePost postInfo) {
        if (postRepository.findById(postInfo.getPostId()).isEmpty()) {
            throw new NotFoundPostException();
        }
        Post updatedPost = postRepository.findByPostId(postInfo.getPostId());
        updatedPost.setContent(postInfo.getContent());
        updatedPost.setUpdate_time(Instant.now());
        postRepository.save(updatedPost);
    }

    /**
     * 특정 게시물을 삭제합니다.
     *
     * @param postId 삭제할 게시물의 ID
     *
     * @throws NotFoundPostException 게시물을 찾을 수 없을 때 발생합니다.
     */
    public void deletePost(Long postId) {
        if (postRepository.findById(postId).isEmpty()) {
            throw new NotFoundPostException();
        }
        postRepository.deleteById(postId);
    }

    /**
     * 앨범 이름으로 앨범을 검색합니다.
     *
     * @param albumName 검색할 앨범 이름
     * @return 해당 앨범의 정보를 포함한 DTO 객체 리스트
     *
     * @throws NotFoundAlbumException 앨범을 찾을 수 없을 때 발생합니다.
     */
    public List<AlbumDto> searchAlbum(String albumName) {
        if (albumRepository.findAllByTitle(albumName).isEmpty()) {
            throw new NotFoundAlbumException();
        }
        return albumRepository.findAllByTitle(albumName)
                .stream()
                .map(Album::toAlbumDto)
                .toList();
    }

    /**
     * 아티스트 이름으로 앨범을 검색합니다.
     *
     * @param artistName 검색할 아티스트 이름
     * @return 해당 아티스트의 앨범 정보를 포함한 DTO 객체 리스트
     *
     * @throws NotFoundArtistException 아티스트를 찾을 수 없을 때 발생합니다.
     */
    public List<AlbumDto> searchArtist(String artistName) {
        if (artistRepository.findByArtistName(artistName).isEmpty()) {
            throw new NotFoundArtistException();
        }
        return albumRepository.findAllByArtist_ArtistName(artistName)
                .stream()
                .map(Album::toAlbumDto)
                .toList();
    }
}
