package org.cosmic.backend.domain.albumChat.applications;

import org.cosmic.backend.domain.albumChat.domains.AlbumChatComment;
import org.cosmic.backend.domain.albumChat.dtos.comment.AlbumChatCommentCreateReq;
import org.cosmic.backend.domain.albumChat.dtos.comment.AlbumChatCommentDto;
import org.cosmic.backend.domain.albumChat.dtos.comment.AlbumChatCommentResponse;
import org.cosmic.backend.domain.albumChat.dtos.comment.AlbumChatCommentUpdateReq;
import org.cosmic.backend.domain.albumChat.exceptions.NotFoundAlbumChatCommentException;
import org.cosmic.backend.domain.albumChat.exceptions.NotFoundAlbumChatException;
import org.cosmic.backend.domain.albumChat.exceptions.NotMatchAlbumChatException;
import org.cosmic.backend.domain.albumChat.repositorys.AlbumChatCommentRepository;
import org.cosmic.backend.domain.albumChat.repositorys.AlbumChatRepository;
import org.cosmic.backend.domain.playList.exceptions.NotFoundUserException;
import org.cosmic.backend.domain.user.repositorys.UsersRepository;
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

    public List<AlbumChatCommentResponse> getCommentsByAlbumChatId(Long albumChatId) {
        List<AlbumChatCommentResponse> comments = new ArrayList<>();
        if(albumChatRepository.findById(albumChatId).isEmpty()) {
            throw new NotFoundAlbumChatException();
        }
        List<AlbumChatComment> commentList = commentRepository.findByAlbumChat_AlbumChatId(albumChatId).get();
        for (AlbumChatComment comment : commentList) {
            AlbumChatCommentResponse commentReq = new AlbumChatCommentResponse(
                comment.getUser().getUserId(),comment.getAlbumChatCommentId(),
                comment.getContent(),comment.getUpdateTime()
            );
            comments.add(commentReq);
        }
        return comments;
    }

    public AlbumChatCommentDto albumChatCommentCreate(AlbumChatCommentCreateReq comment) {

        if(albumChatRepository.findById(comment.getAlbumChatId()).isEmpty()) {
            throw new NotFoundAlbumChatException();
        }
        if(userRepository.findById(comment.getUserId()).isEmpty())
        {
            throw new NotFoundUserException();
        }
        AlbumChatComment commentEntity=new AlbumChatComment(comment.getContent(),
            Instant.now(),userRepository.findByUserId(comment.getUserId()).get(),
        null,albumChatRepository.findById(comment.getAlbumChatId()).get());
        commentRepository.save(commentEntity);
        AlbumChatCommentDto commentDto=new AlbumChatCommentDto();
        commentDto.setAlbumChatCommentId(commentEntity.getAlbumChatCommentId());
        return commentDto;
    }

    public void albumChatCommentUpdate(AlbumChatCommentUpdateReq comment) {
        if(commentRepository.findById(comment.getAlbumChatCommentId()).isEmpty()) {
            throw new NotFoundAlbumChatCommentException();
        }
        AlbumChatComment comment1 = commentRepository.findByAlbumChatCommentId(comment.getAlbumChatCommentId());
        if(!comment1.getAlbumChat().getAlbumChatId().equals(comment.getAlbumChatId())) {
            throw new NotMatchAlbumChatException();
        }
        if(userRepository.findById(comment.getUserId()).isEmpty())
        {
            throw new NotFoundUserException();
        }
        comment1.setContent(comment.getContent());
        commentRepository.save(comment1);

    }

    public void albumChatCommentDelete(AlbumChatCommentDto commentdto) {
        if(commentRepository.findById(commentdto.getAlbumChatCommentId()).isEmpty()) {
            throw new NotFoundAlbumChatCommentException();
        }
        commentRepository.deleteById(commentdto.getAlbumChatCommentId());
    }

    public List<AlbumChatCommentResponse> albumChatCommentSorted(List<Map.Entry<AlbumChatComment, Long>> sortedComments) {

        System.out.println("********hi10");
        List<AlbumChatCommentResponse> comments = new ArrayList<>();
        for (Map.Entry<AlbumChatComment, Long> entry : sortedComments) {
            System.out.println("********hi11"+entry);
            AlbumChatCommentResponse commentReq = new AlbumChatCommentResponse(
                entry.getKey().getUser().getUserId(),entry.getKey().getAlbumChatCommentId(),
                entry.getKey().getContent(),entry.getKey().getUpdateTime()
            );
            System.out.println("********hi12");
            comments.add(commentReq);
        }
        return comments;
    }

}