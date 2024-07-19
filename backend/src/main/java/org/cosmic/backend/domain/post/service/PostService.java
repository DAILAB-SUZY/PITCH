package org.cosmic.backend.domain.post.service;

import org.cosmic.backend.domain.playList.domain.Album;
import org.cosmic.backend.domain.playList.domain.Artist;
import org.cosmic.backend.domain.playList.exceptions.NotFoundArtistException;
import org.cosmic.backend.domain.playList.exceptions.NotFoundUserException;
import org.cosmic.backend.domain.playList.exceptions.NotMatchAlbumException;
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

    public PostReq getPostById(Long postId) {
        //해당 postId인 포스트만 가져온다.
        PostReq postreq = new PostReq();
        if(!postRepository.findById(postId).isPresent())
        {
            throw new NotFoundPostException();
        }
        else{
            Post post=postRepository.findByPostId(postId);
            //postRepository에서 해당 postId로 내용물들 다 가져와서 넣고 반환
            postreq.setCover(post.getCover());
            postreq.setTitle(post.getTitle());
            postreq.setArtistName(post.getArtistName());
            postreq.setContent(post.getContent());
            postreq.setUpdateTime(post.getUpdateTime());
            postreq.setPostId(post.getPostId());
            return postreq;
    }
    }

    public PostDto createPost(CreatePost post) {
        //post생성 버튼 눌렀을 때
        System.out.println("*******"+post);
        if(!userRepository.findById(post.getUserId()).isPresent())
        {
            throw new NotFoundUserException();
        }
        else if(!albumRepository.findByTitleAndArtist_ArtistName(post.getTitle(),post.getArtistName()).isPresent()){
            throw new NotMatchAlbumException();
        }
        else{
            User user=userRepository.findByUserId(post.getUserId()).get();
            Post post1=new Post();
            post1.setTitle(post.getTitle());
            post1.setArtistName(post.getArtistName());
            post1.setContent(post.getContent());
            post1.setUpdateTime(Instant.now());
            post1.setUser(user);
            post1.setCover(post.getCover());
            post1.setComments(null);
            post1.setLikes(null);
            postRepository.save(post1);
            PostDto postDto = new PostDto();
            postDto.setPostId(post1.getPostId());
            return postDto;
        }
    }

    public void updatePost(UpdatePost post) {
        if(!postRepository.findById(post.getPostId()).isPresent())
        {
            throw new NotFoundPostException();
        }
        else {
            Post post1 = postRepository.findByPostId(post.getPostId());
            post1.setContent(post.getContent());
            post1.setUpdateTime(Instant.now());
            postRepository.save(post1);//새로생기는지 업데이트만 되는지 만약 새로생기는거면업데이트만 되게만들어야함.
        }
     }

    public void deletePost(Long postId) {
        if(!postRepository.findById(postId).isPresent())
        {
            throw new NotFoundPostException();
        }
        postRepository.deleteById(postId);
    }
//*
    public List<AlbumDto> searchAlbum(String albumName) {
        //사용자가 앨범 찾기 위해 앨범 이름을 검색할 때
        List<AlbumDto> albums = new ArrayList<>();
        if(albumRepository.findAllByTitle(albumName).get().isEmpty())
        {
            throw new NotFoundAlbumException();
        }
        else {
            List<Album> albumInfo = albumRepository.findAllByTitle(albumName).get();
            for (int i = 0; i < albumInfo.size(); i++) {
                AlbumDto albumDto = new AlbumDto();
                albumDto.setArtistName(albumInfo.get(i).getArtist().getArtistName());
                albumDto.setAlbumName(albumInfo.get(i).getTitle());
                albumDto.setAlbumId(albumInfo.get(i).getAlbumId());
                albums.add(albumDto);
            }
            return albums;
        }
    }

    public List<AlbumDto> searchArtist(String artistName) {
        //사용자가 앨범 찾기 위해 아티스트 이름을 검색할 때
        List<AlbumDto> albums = new ArrayList<>();
        //해당 아티스트이름과 같은 앨범 정보들을 모두 가져와 담음
        if(!artistRepository.findByArtistName(artistName).isPresent())
        {
            throw new NotFoundArtistException();
        }
        else{
            Artist artistInfo= artistRepository.findByArtistName(artistName).get();
            List<Album> album=albumRepository.findAllByArtist_ArtistId(artistInfo.getArtistId());//트랙들을 모두 가져옴
            for(int i=0;i<album.size();i++)
            {
                AlbumDto albumDto = new AlbumDto();
                albumDto.setArtistName(artistName);
                albumDto.setAlbumName(album.get(i).getTitle());
                albumDto.setAlbumId(album.get(i).getAlbumId());
                albums.add(albumDto);
            }
            return albums;
        }

    }

}
