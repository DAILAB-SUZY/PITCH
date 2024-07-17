package org.cosmic.backend.domain.albumChat.service;

import org.cosmic.backend.domain.albumChat.domain.AlbumChatComment;
import org.cosmic.backend.domain.albumChat.dto.comment.AlbumChatCommentDto;
import org.cosmic.backend.domain.albumChat.dto.comment.AlbumChatCommentResponse;
import org.cosmic.backend.domain.albumChat.dto.comment.CreateAlbumChatCommentReq;
import org.cosmic.backend.domain.albumChat.dto.comment.UpdateAlbumChatCommentReq;
import org.cosmic.backend.domain.albumChat.exception.NotFoundAlbumChatCommentException;
import org.cosmic.backend.domain.albumChat.exception.NotFoundAlbumChatException;
import org.cosmic.backend.domain.albumChat.repository.AlbumChatCommentRepository;
import org.cosmic.backend.domain.albumChat.repository.AlbumChatRepository;
import org.cosmic.backend.domain.playList.exceptions.NotFoundUserException;
import org.cosmic.backend.domain.user.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class AlbumChatCommentService {

    @Autowired
    private AlbumChatRepository albumChatRepository;
    @Autowired
    private AlbumChatCommentRepository commentRepository;
    @Autowired
    private UsersRepository userRepository;

    public List<AlbumChatCommentResponse> getCommentsByAlbumChat(Long albumChatId) {
        List<AlbumChatCommentResponse> comments = new ArrayList<>();
        if(!albumChatRepository.findById(albumChatId).isPresent()) {
            throw new NotFoundAlbumChatException();
        }
        else {
            List<AlbumChatComment> commentList = commentRepository.findByAlbumChat_AlbumChatId(albumChatId).get();
            for (AlbumChatComment comment : commentList) {
                AlbumChatCommentResponse commentReq = new AlbumChatCommentResponse();
                commentReq.setAlbumChatCommentId(comment.getAlbumChatCommentId());
                commentReq.setContent(comment.getContent());
                commentReq.setCreateTime(comment.getUpdateTime());
                commentReq.setUserId(comment.getUser().getUserId());
                comments.add(commentReq);
            }
            return comments;
        }
    }

    public AlbumChatCommentDto createAlbumChatComment(CreateAlbumChatCommentReq comment) {//누가 쓴 comment인지 나와야하기 때문에 userId필요
        AlbumChatComment commentEntity=new AlbumChatComment();

        if(!albumChatRepository.findById(comment.getAlbumChatId()).isPresent()) {
            throw new NotFoundAlbumChatException();
        }
        else if(!userRepository.findById(comment.getUserId()).isPresent())
        {
            throw new NotFoundUserException();
        }
        else{
            commentEntity.setContent(comment.getContent());
            commentEntity.setUpdateTime(Instant.now());
            commentEntity.setUser(userRepository.findByUserId(comment.getUserId()).get());
            commentEntity.setAlbumChatReplies(null);
            commentEntity.setAlbumChat(albumChatRepository.findById(comment.getAlbumChatId()).get());
            commentRepository.save(commentEntity);
            AlbumChatCommentDto commentDto=new AlbumChatCommentDto();
            commentDto.setAlbumChatCommentId(commentEntity.getAlbumChatCommentId());
            return commentDto;
        }
    }

    public void updateAlbumChatComment(UpdateAlbumChatCommentReq comment) {
        if(!commentRepository.findById(comment.getAlbumChatCommentId()).isPresent()) {
            throw new NotFoundAlbumChatCommentException();
        }
        else {
            AlbumChatComment comment1 = commentRepository.findByAlbumChatCommentId(comment.getAlbumChatCommentId());
            comment1.setContent(comment.getContent());
            comment1.setUpdateTime(Instant.now());
            commentRepository.save(comment1);//새로생기는지 업데이트만 되는지 만약 새로생기는거면업데이트만 되게만들어야함.
        }
    }

    public void deleteAlbumChatComment(AlbumChatCommentDto commentdto) {
        if(!commentRepository.findById(commentdto.getAlbumChatCommentId()).isPresent()) {
            throw new NotFoundAlbumChatCommentException();
        }
        else{
            commentRepository.deleteById(commentdto.getAlbumChatCommentId());
        }
    }

    public List<AlbumChatCommentResponse> sortedAlbumChatComment(List<Map.Entry<AlbumChatComment, Long>> sortedComments) {

        List<AlbumChatCommentResponse> comments = new ArrayList<>();

        for (Map.Entry<AlbumChatComment, Long> entry : sortedComments) {
            AlbumChatCommentResponse commentReq = new AlbumChatCommentResponse();
            commentReq.setAlbumChatCommentId(entry.getKey().getAlbumChatCommentId());
            commentReq.setContent(entry.getKey().getContent());
            commentReq.setCreateTime(entry.getKey().getUpdateTime());
            commentReq.setUserId(entry.getKey().getUser().getUserId());
            comments.add(commentReq);
        }
        return comments;
    }

}